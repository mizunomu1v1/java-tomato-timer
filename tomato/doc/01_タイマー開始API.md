# ポモドーロタイマー開始API 設計書

## 目次
- [ポモドーロタイマー開始API 設計書](#ポモドーロタイマー開始api-設計書)
  - [目次](#目次)
  - [概要](#概要)
  - [エンドポイント](#エンドポイント)
  - [エンドポイント詳細](#エンドポイント詳細)
  - [処理詳細](#処理詳細)
    - [1. パラメータ取得](#1-パラメータ取得)
    - [2. エラーチェック](#2-エラーチェック)
      - [共通エラーチェック](#共通エラーチェック)
      - [項目エラーチェック](#項目エラーチェック)
    - [3. 変数作成](#3-変数作成)
    - [4. DB登録](#4-db登録)
      - [タイマーヘッダテーブル](#タイマーヘッダテーブル)
      - [タイマー明細テーブル](#タイマー明細テーブル)
    - [5. レスポンスを返却する](#5-レスポンスを返却する)

## 概要
ポモドーロタイマーの開始機能を提供する。

## エンドポイント
- ベース URL: `/tomato`

## エンドポイント詳細

- **HTTP メソッド**: POST
- **URL**: `/api/tomato/timerCreate`
- **リクエストボディ**:
    | 物理名        | 論理名           | 必須 | 型     | 桁  | フォーマット |
    | ------------- | ---------------- | ---- | ------ | --- | ------------ |
    | workDuration  | 作業時間         | Y    | String | 1~2 | 99           |
    | breakDuration | 休憩時間         | Y    | String | 1~2 | 99           |
    | totalSets     | トータルセット数 | Y    | String | 1~2  | 9            |
  
- **レスポンスボディ**:

    | 物理名  |               | 論理名       | 型      | 桁  | フォーマット        |
    | ------- | ------------- | ------------ | ------- | --- | ------------------- |
    | timerId |               | タイマーID   | String | 1~4 | 9999                |
    | sets    |               | セットリスト | array   | 1~1 |                     |
    |         | set           | セット数     | String | 1   | 9                   |
    |         | startWorkTime | 作業開始時刻 | String  | 0   | YYYY-MM-DD HH:MM:SS |
    |         | endWorkTime   | 作業終了時刻 | String  | 0   | YYYY-MM-DD HH:MM:SS |
    |         | endBreakTime  | 休憩終了時刻 | String  | 0   | YYYY-MM-DD HH:MM:SS |

## 処理詳細

### 1. パラメータ取得
- リクエストボディから各パラメータを取得する
- システム日時を取得する

### 2. エラーチェック
- 受け取ったパラメータに対して以下のエラーチェックを実施する

#### 共通エラーチェック
  - **必須チェック**: 必須項目が存在すること
  - **桁数チェック**: 各パラメータが指定の桁数内であること
 
#### 項目エラーチェック
  - **作業時間**: 1以上、60未満であること
  - **休憩時間**: 1以上、60未満であること
  - **トータルセット数**: 1以上、10未満であること

### 3. 変数作成
- **timerId**: UUIDを使用して一意のIDを生成する
- **sets**: setCount分以下を繰り返し配列を作成する
  - **startWorkTime**: 
    - `変数.setCount`=1の場合、システム日時を設定する
    - `変数.setCount`<>1の場合、前セットの`変数.endBreakTime`を設定する
  - **endWorkTime**: `変数.startWorkTime` + `workTime`
  - **endBreakTime**: `変数.endBreakTime` + `breakTime`

### 4. DB登録

#### タイマーヘッダテーブル

```sql
INSERT INTO 
  TIMER_HEADERS (
    timer_id
    ,work_duration
    ,break_duration
    ,total_sets
    ,status
    ,insert_time
    ,insert_function
    ,update_time
    ,update_function
  )
VALUES(
    変数.timerId,
    変数.workDuration,
    変数.breakDuration,
    変数.totalSets,
    'running',
    CURRENT_TIMESTAMP,
    '/api/tomato/start',
    CURRENT_TIMESTAMP,
    '/api/tomato/start'
  );

```

#### タイマー明細テーブル

- setCount分以下を繰り返しINSERTする

```sql
INSERT INTO 
  TIMER_LINES (
    timer_id
    ,set_count
    ,start_work_time
    ,end_work_time
    ,end_break_time
    ,insert_time
    ,insert_function
    ,update_time
    ,update_function
  )
VALUES(
    変数.timerId,
    変数.setCount,
    変数.startWorkTime,
    変数.endWorkTime,
    変数.endBreakTime,
    CURRENT_TIMESTAMP,
    '/api/tomato/start',
    CURRENT_TIMESTAMP,
    '/api/tomato/start'
  );

```

### 5. レスポンスを返却する
- レスポンスオブジェクトを作成して返却する
