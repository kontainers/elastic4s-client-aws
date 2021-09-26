package com.sksamuel.elastic4s.aws

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.net.URLEncoder

class CanonicalRequestTest extends AnyWordSpec with Matchers with SharedTestData {

  "CanonicalRequest" should {

    val resultWithoutPayload =
      s"""GET
        |/${URLEncoder.encode("https://es.amazonaws.com", "iso-8859-1")}/path/to/resource
        |Action=ListUsers&Version=2010-05-08
        |content-type:application/x-www-form-urlencoded; charset=utf-8
        |host:es.amazonaws.com
        |x-amz-date:20150830T123600Z
        |
        |content-type;host;x-amz-date
        |e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855""".stripMargin

    val resultWithForbiddenCharacters =
      s"""GET
         |/${URLEncoder.encode("https://es.amazonaws.com", "iso-8859-1")}/path/to/resource$encodedForbiddenCharactersAndMore
         |Action=ListUsers&Version=2010-05-08
         |content-type:application/x-www-form-urlencoded; charset=utf-8
         |host:es.amazonaws.com
         |x-amz-date:20150830T123600Z
         |
         |content-type;host;x-amz-date
         |e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855""".stripMargin

    val resultWithPayload =
      s"""POST
        |/${URLEncoder.encode("https://es.amazonaws.com", "iso-8859-1")}/path/to/resource
        |Action=ListUsers&Version=2010-05-08
        |content-type:application/x-www-form-urlencoded; charset=utf-8
        |host:es.amazonaws.com
        |x-amz-date:20150830T123600Z
        |
        |content-type;host;x-amz-date
        |9a5de716a2c94f29b95a8cbfab004d1c8c8c5c1fd8781f1fe7916796b7633b95""".stripMargin

    "be able to build a canonical request string from request without payload" in {
      val canonicalRequest = CanonicalRequest(httpGetRequest)
      canonicalRequest.toString shouldBe(resultWithoutPayload)
    }

    "be able to build a canonical request string from request with unordered query params" in {
      val canonicalRequest = CanonicalRequest(httpGetRequestWithUnorderedQueryParams)
      canonicalRequest.toString shouldBe(resultWithoutPayload)
    }

    "be able to build a canonical request string from request with payload" in {
      val canonicalRequest = CanonicalRequest(httpPostRequest)
      canonicalRequest.toString shouldBe(resultWithPayload)
    }

    "be able to be encode a url with forbidden characters making sure it follows RFC 3986" in {
      val canonicalRequest = CanonicalRequest(httpWithForbiddenCharacters)
      canonicalRequest.toString shouldBe(resultWithForbiddenCharacters)
    }

  }
}
