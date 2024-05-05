package com.paranid5.auth_service.data.oauth.client

import cats.Applicative
import cats.syntax.all.*

import com.paranid5.auth_service.data.oauth.client.entity.AppEntity

trait AppDataSource[F[_] : Applicative, S]:
  extension (source: S)
    infix def getClientApps(clientId: Long): F[List[AppEntity]]

    def getApp(
      appId:     Long,
      appSecret: String
    ): F[Option[AppEntity]]

    def insertApp(
      appId:        Long,
      appSecret:    String,
      appName:      String,
      appThumbnail: Option[String],
      callbackUrl:  Option[String],
      clientId:     Long,
    ): F[Unit]

    infix def insertApp(app: AppEntity): F[Unit] =
      source.insertApp(
        appId        = app.appId,
        appSecret    = app.appSecret,
        appName      = app.appName,
        appThumbnail = app.appThumbnail,
        callbackUrl  = app.callbackUrl,
        clientId     = app.clientId,
      )

    infix def insertApps(apps: List[AppEntity]): F[Unit] =
      apps
        .map(source insertApp _)
        .sequence
        .map(_ ⇒ ())

    infix def deleteApp(appId: Long): F[Unit]

    infix def deleteClientApps(clientId: Long): F[Unit]

    def updateApp(
      appId:           Long,
      newAppName:      String,
      newAppThumbnail: Option[String],
      newCallbackUrl:  Option[String],
    ): F[Unit]
