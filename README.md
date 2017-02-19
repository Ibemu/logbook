# 艦これ 航海日誌+
@sanae_hirotaka 氏の[航海日誌](http://kancolle.sanaechan.net/)を自分用に改造したものです。
不具合の報告は[GitHubの報告ページ](https://github.com/Ibemu/logbook/issues/new)か[Twitter](https://twitter.com/Ibemu231)にお願いします。

## 機能
[本家](http://kancolle.sanaechan.net/) の機能に加えて、

* JSONを保存するオプションの復活
* 受信した全てのJSONを保存
* 任務カウンター
* 艦隊タブに表示する情報の追加
* 大破警告(進撃判定前)
* 出撃報告(戦闘ランク予測)
* 艦隊タブのウィンドウ化
* 不具合(と思われるもの)の修正
* 他

を実装しています。

## 導入方法
1. 本家の航海日誌をまだ導入していない場合はインストールします(起動する必要はありません)
2. [リリースページ](https://github.com/Ibemu/logbook/releases)からlogbook.jarをダウンロードし同じファイルを置き換えます

## 作者
### 本家
@sanae_hirotaka 氏

### 改造
@Ibemu231

## チェンジログ
### 0.8.1+1.0.0
* 初版

### 0.8.2+1.0.1
* 本家0.8.2に更新
* バージョンダイアログに航海日誌+のサイトへのリンクを追加
* 遠征がすぐに反映されるように変更

### 0.8.3+1.0.2
* 本家0.8.3に更新
  * 必要ライブラリが増えているので[本家](http://kancolle.sanaechan.net/)を最新版に更新してから導入して下さい
* 出撃報告の表示位置とレイアウトを保存するように変更

### 0.8.4+1.0.3
* 本家0.8.4に更新
* 出撃報告で退避を反映するように変更

### 0.8.5+1.0.4
* 本家0.8.5に更新
* 以下の場合の戦闘を解析するときににエラーが発生するバグを修正
  * 一部の夜戦
  * 支援艦隊が到着した場合
* 出撃報告で連合艦隊の種類を表示するように変更

### 0.8.6+1.0.5
* 本家0.8.6に更新
* 軽微な修正

### 0.8.7+1.0.5
* 本家0.8.7に更新

### 0.8.8+1.0.6
* 本家0.8.8に更新
* 内部的な修正(本家のリファクタリングに合わせて)

### 0.9+1.1.0
* 本家0.9に更新
* 艦隊タブをダブルクリックでダイアログ化できるようにした
* 内部的な修正(本家のリファクタリングに合わせて)

### 0.9.1+1.1.1
* 本家0.9.1に更新
* 補給した時などに艦隊タブダイアログが更新されない不具合を修正
* 所有艦娘一覧の「成長の余地」設定で補強増設欄がない不具合を修正
  * 確認してないけど落ちるのでは

### 0.9.2+1.1.1
* 本家0.9.2に更新

### 0.9.2+1.1.2
* 連合艦隊戦闘時に開幕雷撃の被ダメを第1艦隊で処理していた不具合を修正

  #### 追加
  * 誰もいない艦隊を変更すると落ちることがある不具合を修正
  * 艦娘を外して誰もいない状態にすると落ちることがある不具合を修正

### 0.9.3+1.1.3
* 本家0.9.3に更新
  * 必要ライブラリが変わっているので[本家](http://kancolle.sanaechan.net/)を最新版に更新してから導入して下さい
* フィルタに出撃海域を追加
* ダメージ計算でダメコンの処理を仮実装
* 出撃報告に制空状態と索敵状態を追加

### 0.9.4+1.1.4
* 本家0.9.4に更新
* ダメージ計算の修正
  * 轟沈(ダメコン発動を含む)時にエラーが出ることがある
  * 連合艦隊を作った状態で通常艦隊を出撃させると正しく計算されない
* 出撃報告を縦長表示した時に文字が枠からはみ出す不具合を修正

### 0.9.5+1.1.5
* 本家0.9.5に更新
* 所有艦娘一覧・任務一覧の追加列の再実装(本家に合わせて)

### 0.9.6+1.1.5
* 本家0.9.6に更新

### 0.9.6+1.1.6
* 出撃報告で輸送護衛部隊に対応

### 0.9.7+1.1.6
* 本家0.9.7に更新

### 0.9.8+1.1.6
* 本家0.9.8に更新

### 0.9.9+1.1.7
* 本家0.9.9に更新
* 2016冬E3の空襲マス対応

### 0.9.11+1.1.8
* 本家0.9.11に更新
* 出撃報告に触接とギミックを追加

### 0.9.11+1.1.9
* 16/06/10 アプデ対応
* 16/06/30 アプデ対応(仮)
* 任務カウンタのタイムゾーンをJST固定にした

### 0.9.11+1.2.0
* 新規艦娘着任通知
* 出撃海域の自動追加(仮)
  * 名前が「札#」に統一されます
* 制空値計算修正
* 艦隊タブでの搭載機数表示

### 0.9.11+1.3.0
* 敵連合艦隊・連合vs連合対応
* 艦娘マスタ読み込みのバグ修正
* リファクタリング

### 0.9.11+1.3.1
* 水上打撃部隊の対応漏れ