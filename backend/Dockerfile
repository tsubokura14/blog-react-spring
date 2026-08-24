# --- build stage ---
# JDK(コンパイラ等の開発ツール込み)入りの環境。ビルドにしか使わない
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# 依存関係まわりのファイルだけ先にコピー(ソース変更時のキャッシュ効率のため)
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew --version

# ソースコードをコピーして、実行可能jar(fat jar)をビルドする
# --no-daemon: コンテナは使い捨てなのでGradle常駐プロセスは不要
COPY src src
RUN ./gradlew bootJar --no-daemon

# --- run stage ---
# JRE(実行環境のみ)の軽量な環境。ここからが実際に配布・起動するイメージ
FROM eclipse-temurin:17-jre
WORKDIR /app

# buildステージで作ったjarだけを持ってくる(JDK・ソース・Gradleキャッシュは含めない)
COPY --from=build /app/build/libs/*.jar app.jar

# Spring Bootのデフォルトポート(ドキュメント目的。実際の公開はdocker-compose側で設定)
EXPOSE 8080

# コンテナ起動時に実行するコマンド。ローカルのbootRunと最終的に同じことをしている
ENTRYPOINT ["java", "-jar", "app.jar"]