package uk.gov.nationalarchives.tdr.common.utils.objectkeycontext

import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.AssetSources.AssetSource
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.Buckets.{DirtyUploadBucketPrefix, ExportBucketPrefix}
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectCategories.ObjectCategory
import uk.gov.nationalarchives.tdr.common.utils.objectkeycontext.ObjectTypes.{ObjectType, Record}

import java.util.UUID
import scala.util.{Failure, Success, Try}

object Context {
  case class ObjectKeyContext(
                               userId: Option[UUID] = None,
                               transferId: Option[UUID] = None,
                               assetSource: Option[AssetSource] = None,
                               category: Option[ObjectCategory] = None,
                               objectType: Option[ObjectType] = None,
                               objectName: Option[String] = None,
                               assetId: Option[UUID] = None,
                               fileId: Option[UUID] = None,
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
      ObjectKeyContext(transferId = Some(transferId), objectType = Some(objectType), objectName = Some(objectName))
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
      ObjectKeyContext(
        Some(userId),
        Some(transferId),
        Some(assetSource),
        Some(objectCategory),
        objectType, objectName)
    } match {
      case Failure(ex)               => throw new RuntimeException(s"Invalid object key $objectKey: ${ex.getMessage}")
      case Success(objectKeyContext) => objectKeyContext
    }
  }

  private def exportObjectKeyParser(elements: List[String], objectKey: String): ObjectKeyContext = {
    Try {
      if (elements.size == 2) {
        val assetId = UUID.fromString(elements.head)
        val fileId = UUID.fromString(elements.last)
        val objectType = getObjectType(elements.last)
        ObjectKeyContext(objectType = Some(objectType), objectName = Some(elements.last), assetId = Some(assetId), fileId = Some(fileId))
      } else {
        val assetElements = elements.head.split("\\.")
        val assetId = UUID.fromString(assetElements.head)
        val objectType = getObjectType(objectKey)
        ObjectKeyContext(objectType = Some(objectType), objectName = elements.headOption, assetId = Some(assetId))
      }
    } match {
      case Failure(ex) => throw new RuntimeException(s"Invalid object key $objectKey: ${ex.getMessage}")
      case Success(objectKeyContext) => objectKeyContext
    }
  }

  /**
   * Method parse AWS S3 object key returning its context
   *
   * Supports two forms of object key:
   * - default key: {consignment id}/{object}
   * - upload bucket key: {user id}/{asset source}/{consignment id}/{object category}/{object}
   * - export bucket keys: {asset id}.metadata; or {asset id}/{object}
   *
   * @param objectKey
   * Key of the object
   *
   * @return
   * ObjectKeyContext
   * */
  def objectKeyParser(objectKey: String, bucketName: String): ObjectKeyContext = {
    val elements = objectKey.split('/').toList

    bucketName match {
      case _ if bucketName.startsWith(DirtyUploadBucketPrefix.id) => uploadObjectKeyParser(elements, objectKey)
      case _ if bucketName.startsWith(ExportBucketPrefix.id)      => exportObjectKeyParser(elements, objectKey)
      case _                                                      => defaultObjectKeyParser(elements, objectKey)
    }
  }
}
