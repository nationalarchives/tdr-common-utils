package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.AssetSources.AssetSource
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectCategories.ObjectCategory
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectTypes.{ObjectType, Record}

import java.util.UUID
import scala.util.{Failure, Success, Try}

object Context {
  case class ObjectKeyContext(
                               userId: Option[UUID] = None,
                               transferId: UUID,
                               assetSource: Option[AssetSource] = None,
                               category: Option[ObjectCategory] = None,
                               objectType: Option[ObjectType] = None,
                               objectName: Option[String] = None
                             )

  private def getObjectType(element: String): ObjectType = {
    if (element.contains(".")) {
      ObjectTypes.toObjectType(element.split('.').last)
    } else Record
  }

  private def isPrefixKey(elements: List[String]): Boolean = {
    elements.size == 4
  }

  private def defaultObjectKeyParser(elements: List[String], objectKey: String): ObjectKeyContext = {
    Try {
      val objectName = elements.last
      val objectType = getObjectType(objectName)
      val transferId = UUID.fromString(elements.head)
      ObjectKeyContext(transferId = transferId, objectType = Some(objectType), objectName = Some(objectName))
    } match {
      case Failure(ex)               => throw new RuntimeException(s"Invalid object key $objectKey: ${ex.getMessage}")
      case Success(objectKeyContext) => objectKeyContext
    }
  }

  private def uploadObjectKeyParser(elements: List[String], objectKey: String): ObjectKeyContext = {
    Try {
      val prefixKey = isPrefixKey(elements)
      val objectName = if (prefixKey) None else Some(elements.last)
      val objectType = if (prefixKey) None else Some(getObjectType(objectName.get))
      val objectCategory = ObjectCategories.toObjectCategory(elements(3))
      val transferId = UUID.fromString(elements(2))
      val assetSource = AssetSources.toAssetSource(elements(1))
      val userId = UUID.fromString(elements.head)
      ObjectKeyContext(Some(userId), transferId, Some(assetSource), Some(objectCategory), objectType, objectName)
    } match {
      case Failure(ex)               => throw new RuntimeException(s"Invalid object key $objectKey: ${ex.getMessage}")
      case Success(objectKeyContext) => objectKeyContext
    }
  }

  /**
   * Method parse AWS S3 object key returning its context
   *
   * Supports two forms of object key:
   * - default key: {consignment id}/{object}
   * - upload key: {user id}/{asset source}/{consignment id}/{object category}/{object}
   *
   * @param objectKey
   * Key of the object
   *
   * @return
   * ObjectKeyContext
   * */
  def objectKeyParser(objectKey: String): ObjectKeyContext = {
    val elements = objectKey.split('/').toList
    elements.size match {
      case 5 | 4 => uploadObjectKeyParser(elements, objectKey)
      case _     => defaultObjectKeyParser(elements, objectKey)
    }
  }
}
