package uk.gov.nationalarchives.tdr.common.utils.records

object Records {
  private val retentionTypeField = "retention_type"

  def retainedRecord(context: RecordContext): Boolean = {
    context.metadata.map(_.field).contains(retentionTypeField)
  }

  case class Metadata(field: String, value: String)
  case class RecordContext(metadata: List[Metadata])
}
