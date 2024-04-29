package com.paranid5.auth_service

import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all.*

import com.comcast.ip4s.{ipv4, port}

import com.paranid5.auth_service.auth.authRouter
import com.paranid5.auth_service.oauth.oauthRouter
import com.paranid5.auth_service.token.generateToken

import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.{Request, Response}

object App extends IOApp:
  private def sendToken(name: String): IO[Response[IO]] =
    Ok(generateToken(name).map(_.getOrElse("")))

  private def authService: Kleisli[IO, Request[IO], Response[IO]] =
    (authRouter <+> oauthRouter).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"4000")
      .withHttpApp(authService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)