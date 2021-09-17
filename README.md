# 環境構築

## keycloakサーバの設定

以下のドキュメントを参考に、keycloakサーバを起動し開発者コンソールにログインします。

https://keycloak-documentation.openstandia.jp/master/ja_JP/getting_started/index.html

keycloakのポートは8180にして起動すること。

```
./standalone.sh -Djboss.socket.binding.port-offset=100
```

## レルムの作成

masterレルムの管理者として、管理者がユーザーとアプリケーションを作成するレルムを作成します。

手順

1. http://localhost:8180/auth/admin/ に移動し、adminアカウントを使用してKeycloak管理コンソールにログインします。
2. Master のメニューから Add Realm を選択し、クリックします。masterレルムにログインすると、このメニューには他のすべてのレルムが一覧表示されます。
3. Name フィールドに ktor-keycloak-sample と入力します。
4. Create をクリックします。 メインの管理コンソールページが開き、レルムが ktor-keycloak-sample に設定されます。

## ユーザーの作成

ktor-keycloak-sample レルムで、新しいユーザーとその新しいユーザーの一時的なパスワードを作成します。

手順

1. メニューから Users をクリックして、ユーザー一覧ページを開きます。
2. ユーザー追加ページを開くには、空のユーザーリストの右側にある Add User をクリックします。
3. Username フィールドに名前を入力します。 これは唯一の必須フィールドです。 Email Verified スイッチを On に切り替え、 Save をクリックします。
4. 新しいユーザーの管理ページが開きます。
5. Credentials タブをクリックして、新しいユーザーの仮パスワードを設定します。
6. 新しいパスワードを入力して確認します。
7. ユーザーのパスワードを指定した新しいパスワードにするには、Set Password をクリックします。
    - このパスワードは一時的なもので、最初のログイン時に変更する必要があります。永続的なパスワードを作成する場合は、 Temporary スイッチを Off に切り替えて、 Set Password をクリックします。

## アカウント・コンソールへのログイン

レルム内のすべてのユーザーは、アカウント・コンソールにアクセスできます。このコンソールを使用して、プロファイル情報を更新し、クレデンシャルを変更します。これで、作成したレルムでそのユーザーを使用してログインをテストできます。

手順

1. ユーザーメニューを開き、 Sign Out を選択して、管理コンソールからログアウトします。
2. http://localhost:8180/auth/realms/ktor-keycloak-sample/account に移動し、作成したユーザーとして ktor-keycloak-sample レルムにログインします。
3. Personal Info画面で、email・first name・last nameを設定します。

## ktor-keycloak-sampleアプリケーションの登録

Keycloak管理コンソールでクライアントを定義および登録できます。

手順

1. 管理者アカウントで管理コンソールにログインします： http://localhost:8180/auth/admin/
2. 左上のドロップダウン・リストで ktor-keycloak-sample レルムを選択します。
3. 左側のメニューの Clients をクリックして、クライアント・ページを開きます。
4. 右側にある Create をクリックします。
5. Add Clientダイアログで、次のようにフィールドに入力して、 ktor-keycloak-sample というクライアントを作成します。
   1. Client ID: ktor-keycloak-sample
   2. Valid Redirect URIs: http://localhost:8080/*
6. Save をクリックします。
7. 表示される ktor-keycloak-sample クライアントページで、 Installation タブをクリックします。
8. Keycloak OIDC JSON を選択して、後の手順で必要なファイルを生成します。
9. Download をクリックして Keycloak.json を後で見つけられる場所に保存します。

## .envファイルの作成

1. .env.sample ファイルをコピーして .env ファイルを作成します。
   - `cp .env.sample .env`
2. .env ファイルにkeycloakの設定を記述します。手順通りに進めた場合は、以下のようになります。

```
KEYCLOAK_AUTHORIZE_URL=http://localhost:8180/auth/realms/ktor-keycloak-sample/protocol/openid-connect/auth
KEYCLOAK_ACCESS_TOKEN_URL=http://localhost:8180/auth/realms/ktor-keycloak-sample/protocol/openid-connect/token
KEYCLOAK_CLIENT_ID=ktor-keycloak-sample
KEYCLOAK_CLIENT_SECRET=
```
