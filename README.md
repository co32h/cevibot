# cevibot
<br>
Discordのボイスチャンネル内でテキストメッセージをCeVIOのトーク音源のキャストたちに読み上げさせるBot、のプログラム
<br>
以下、zipに同梱しているreadmeと同内容
----------------------------------------

[プログラム名]　　　cevibot<br>
[バージョン]　　　　0.1.0<br>
[動作確認環境]　　　Windows10<br>
[開発言語]　　　　　Kotlin 1.2.50<br>
[作成者]　　　　　　Hinyari<br>
[最初の配布]　　　　2018/06/15<br>
[最終更新]　　　　　2018/06/15<br><br><br>


[cevibotについて]<br>
Discordのサーバのテキストメッセージをボイスチャンネルで読み上げるBotのプログラムです。<br>
実行には以下の3つが必要です。<br>
1.Java8の実行環境<br>
2.DiscordのBot<br>
3.CeVIO Creative Studioと1人以上のトーク音源<br>
Botのセットアップに際しては手順の解説動画を用意する予定です。<br><br><br>


[使用方法]<br>
1.DiscordのウェブサイトからBotを作成し、サーバに導入します。<br>
　●サーバに導入する際、Bot Permissionsは以下の4つにチェックを入れてください。<br>
　　・View Channels (GENERAL PERMISSIONS内)<br>
　　・Send Messages (TEXT PERMISSIONS内)<br>
　　・Connect (VOICE PERMISSIONS内)<br>
　　・Speak (VOICE PERMISSIONS内)<br>
2.トークンなどの設定をコンフィグファイルに記入します。チャンネルIDなどはDiscordの開発者モードをオンにすることで取得できるようになります。<br>
3.jarファイルをダブルクリックで起動します。CeVIO Creative Studioは本プログラムの起動時に同時に起動するため、手動で起動する必要はありません。終了時も同様です。
<br><br><br>

[同梱しているもの]<br>
cevibot-0.1.0.jar<br>
readme.txt<br>
cevibot<br>
└ config.txt<br>
└ icon.png<br>
└ temp<br>
<br>
●cevibot-0.1.0.jar<br>
　Botのプログラム本体です。<br>
●readme.txt<br>
　今読んでいるリードミーテキストです。<br>
●cevibot<br>
　コンフィグファイルなどが入っているフォルダです。<br>
●config.txt<br>
　Botのトークンなどを設定するコンフィグファイルです。各設定項目についてはコンフィグファイル内の記述を参考にしてください。<br>
●icon.png<br>
　タスクトレイに表示されるこのプログラムのアイコン画像です。<br>
●temp<br>
　読み上げ時に出力されるwavの出力先フォルダです。ダウンロード直後では何も入っていませんが、削除しないでください。<br>
<br><br>

[不要になったら]<br>
jarファイルの入っているフォルダを削除してください。<br>
<br>
<br>
[免責事項]<br>
本プログラムを使用したことによって生じたいかなる損害に関し、著作者であるHinyariは一切の責任を負いません。各自の責任においてご利用ください。<br>
<br>
<br>
[変更履歴]<br>
0.1.0 2018/06/15<br>
　公開<br>
<br>
<br>
以上<br>
