package com.paranid5.auth_service.routing.oauth.response

import cats.effect.IO

import com.paranid5.auth_service.data.oauth.token.entity.TokenEntity
import com.paranid5.auth_service.routing.oauth.entity.TokenResponse

import io.circe.syntax.*

import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.Response
import org.http4s.dsl.io.*

def tokensGenerated(
  accessToken:  TokenEntity,
  refreshToken: TokenEntity,
  redirectUrl:  String,
): IO[Response[IO]] =
  Created:
    TokenResponse(
      accessToken  = accessToken,
      refreshToken = refreshToken,
      redirectUrl  = redirectUrl,
    ).asJson
