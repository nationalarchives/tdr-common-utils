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
                               objectType: ObjectType,
                               objectName: String
                             )

  private def getObjectType(element: String): ObjectType = {
    if (element.contains(".")) {
      ObjectTypes.toObjectType(element.split('.').last)
    } else Record
  }

  private def defaultObjectKeyParser(elements: List[String], objectKey: String): ObjectKeyContext = {
    Try {
      val objectName = elements.last
      val objectType = getObjectType(objectName)
      val transferId = UUID.fromString(elements.head)
      ObjectKeyContext(transferId = transferId, objectType = objectType, objectName = objectName)
    } match {
      case Failure(ex) => throw new RuntimeException(s"Invalid object key $objectKey: ${ex.getMessage}")
      case Success(objectKeyContext) => objectKeyContext
    }
  }

  private def uploadObjectKeyParser(elements: List[String], objectKey: String): ObjectKeyContext = {
    Try {
      val keyElements = elements.reverse
      val objectName = keyElements.head
      val objectType = getObjectType(objectName)
      val objectCategory = ObjectCategories.toObjectCategory(keyElements(1))
      val transferId = UUID.fromString(keyElements(2))
      val assetSource = AssetSources.toAssetSource(keyElements(3))
      val userId = UUID.fromString(keyElements.last)
      ObjectKeyContext(Some(userId), transferId, Some(assetSource), Some(objectCategory), objectType, objectName)
    } match {
      case Failure(ex)               => throw new RuntimeException(s"Invalid object key $objectKey: ${ex.getMessage}")
      case Success(objectKeyContext) => objectKeyContext
    }
  }

  /**
   * Method parse AWS S3 object key returning its context
   * @param objectKey
   * Key of the object
   *
   * @return
   * ObjectKeyContext
   * */
  def objectKeyParser(objectKey: String): ObjectKeyContext = {
    val elements = objectKey.split('/').toList
    elements.size match {
      case 5 => uploadObjectKeyParser(elements, objectKey)
      case _ => defaultObjectKeyParser(elements, objectKey)
    }
  }
}
