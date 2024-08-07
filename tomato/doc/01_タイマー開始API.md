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
- **URL**: `/api/tomato/start`
- **リクエストボディ**:
    | 物理名        | 論理名   | 必須 | 型      | 桁  |
    | ------------- | -------- | ---- | ------- | --- |
    | workDuration  | 作業時間 | Y    | integer | 2   |
    | breakDuration | 休憩時間 | Y    | integer | 2   |
    | totalSets     | セット数 | Y    | integer | 2   |
  
- **レスポンスボディ**:

    | 物理名  |               | 論理名       | 型        | 桁  |
    | ------- | ------------- | ------------ | --------- | --- |
    | timerId |               | タイマーID   | UUID      | 3   |
    | sets    |               | セットリスト | array     | 2   |
    |         | startWorkTime | 作業開始時刻 | timestamp | 0   |
    |         | endWorkTime   | 作業終了時刻 | timestamp | 0   |
    |         | endBreakTime  | 休憩終了時刻 | timestamp | 0   |

## 処理詳細

### 1. パラメータ取得
- リクエストボディから各パラメータを取得する
- システム日時を取得する

### 2. エラーチェック
- 共通エラーチェック
  - **必須チェック**: 必須項目の存在を確認する
  - **型チェック**: 各パラメータが整数型であることを確認する
  
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
