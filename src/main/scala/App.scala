package com.paranid5.authback

import com.paranid5.authback.token.generateToken

import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}

import com.comcast.ip4s.{ipv4, port}

import org.http4s.dsl.io.*
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.ember.server.EmberServerBuilder

object App extends IOApp:
  private def onTest(name: String): IO[Response[IO]] =
    Ok(generateToken(name).map(_.getOrElse("")))

  private def authBackService: Kleisli[IO, Request[IO], Response[IO]] =
    HttpRoutes
      .of[IO]:
        case GET → (Root / "test" / name) ⇒ onTest(name)
        case _                            ⇒ NotFound("Unknown request")
      .orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"4000")
      .withHttpApp(authBackService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)