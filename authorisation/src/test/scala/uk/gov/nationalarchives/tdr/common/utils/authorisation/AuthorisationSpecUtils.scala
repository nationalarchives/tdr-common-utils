package uk.gov.nationalarchives.tdr.common.utils.authorisation

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.{forbidden, okJson, post, serverError, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import scala.io.Source.fromResource

class AuthorisationSpecUtils extends AnyFlatSpec with BeforeAndAfterEach with BeforeAndAfterAll {
  val wiremockGraphqlServer = new WireMockServer(9001)

  val graphQlPath = "/graphql"

  def graphQlUrl: String = wiremockGraphqlServer.url(graphQlPath)

  def graphqlGetConsignment(filename: String): StubMapping = wiremockGraphqlServer.stubFor(post(urlEqualTo(graphQlPath))
    .willReturn(okJson(fromResource(s"json/$filename.json").mkString)))

  def graphqlReturnForbiddenError: StubMapping = wiremockGraphqlServer.stubFor(post(urlEqualTo(graphQlPath))
    .willReturn(forbidden()))

  def graphqlReturnServerError: StubMapping = wiremockGraphqlServer.stubFor(post(urlEqualTo(graphQlPath))
    .willReturn(serverError()))

  override def beforeEach(): Unit = {
    wiremockGraphqlServer.resetAll()
  }

  override def beforeAll(): Unit = {
    wiremockGraphqlServer.start()
  }

  override def afterAll(): Unit = {
    wiremockGraphqlServer.stop()
  }
}
