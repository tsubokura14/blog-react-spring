# blog_react_spring(SpringLog 仮称)

React(フロントエンド) + Spring Boot(バックエンド) + PostgreSQL(DB) を使った、認証付きブログ/掲示板アプリ。
ユーザー・投稿・コメント・タグといった機能を通じて、Spring Bootの機能を一通り学ぶことを目的とする。

## 構成

モノレポ構成とし、フロントエンドとバックエンドを1つのリポジトリにまとめています。

```
blog_react_spring/
├── backend/    # Spring Boot
└──frontend/   # React
```

- **backend**: Spring Boot 4.1.1 / Java 17 / Gradle(ラッパー同梱)
- **frontend**: React(Vite想定)
- **DB**: PostgreSQL(導入予定)