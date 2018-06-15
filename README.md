# cevibot
Discordのボイスチャンネル内でテキストメッセージをCeVIOのトーク音源のキャストたちに読み上げさせるBot、のプログラム
以下、zipに同梱しているreadmeと同内容
----------------------------------------

[プログラム名]　　　cevibot
[バージョン]　　　　0.1.0
[動作確認環境]　　　Windows10
[開発言語]　　　　　Kotlin 1.2.50
[作成者]　　　　　　Hinyari
[最初の配布]　　　　2018/06/15
[最終更新]　　　　　2018/06/15


[cevibotについて]
Discordのサーバのテキストメッセージをボイスチャンネルで読み上げるBotのプログラムです。
実行には以下の3つが必要です。
1.Java8の実行環境
2.DiscordのBot
3.CeVIO Creative Studioと1人以上のトーク音源
Botのセットアップに際しては手順の解説動画を用意する予定です。


[使用方法]
1.DiscordのウェブサイトからBotを作成し、サーバに導入します。
　●サーバに導入する際、Bot Permissionsは以下の4つにチェックを入れてください。
　　・View Channels (GENERAL PERMISSIONS内)
　　・Send Messages (TEXT PERMISSIONS内)
　　・Connect (VOICE PERMISSIONS内)
　　・Speak (VOICE PERMISSIONS内)
2.トークンなどの設定をコンフィグファイルに記入します。チャンネルIDなどはDiscordの開発者モードをオンにすることで取得できるようになります。
3.jarファイルをダブルクリックで起動します。CeVIO Creative Studioは本プログラムの起動時に同時に起動するため、手動で起動する必要はありません。終了時も同様です。


[同梱しているもの]
cevibot-0.1.0.jar
readme.txt
cevibot
└ config.txt
└ icon.png
└ temp

●cevibot-0.1.0.jar
　Botのプログラム本体です。
●readme.txt
　今読んでいるリードミーテキストです。
●cevibot
　コンフィグファイルなどが入っているフォルダです。
●config.txt
　Botのトークンなどを設定するコンフィグファイルです。各設定項目についてはコンフィグファイル内の記述を参考にしてください。
●icon.png
　タスクトレイに表示されるこのプログラムのアイコン画像です。
●temp
　読み上げ時に出力されるwavの出力先フォルダです。ダウンロード直後では何も入っていませんが、削除しないでください。


[不要になったら]
jarファイルの入っているフォルダを削除してください。


[免責事項]
本プログラムを使用したことによって生じたいかなる損害に関し、著作者であるHinyariは一切の責任を負いません。各自の責任においてご利用ください。


[変更履歴]
0.1.0 2018/06/15
　公開


以上
