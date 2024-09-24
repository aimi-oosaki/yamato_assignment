package com.example.demo;

import java.io.*;
import java.util.*;


/**
 * タブで列が区切られたTSVデータを読み込み、
 * 第一正規化してTSVデータを出力
 *
 * 仕様からの逸脱点:
 * 本プログラムにはデータのバリデーション処理が含まれていません。
 * 出力する際、データのソートはしていません。
 *
 */
public class FirstNormalization {

	/**
	 * コマンドライン引数として指定されたファイルを読み込み、その内容を正規化
	 *
	 * @param args 第一正規化を行うTSVファイル名
	 * @throws IOException 入力ストリームの読み取り中にエラーが発生した場合にスローされる
	 */
	public static void main(String[] args) throws IOException {
		// ファイルの指定がなければエラーを表示して終了
		if (args.length == 0) {
			System.err.println("エラー: ファイル名を指定してください。");
			return;
		}

		// 指定されたファイルを入力ストリームに使用
		try (FileInputStream fis = new FileInputStream(args[0])) {
			normalize(fis);
		} catch (FileNotFoundException e) {
			System.err.println("エラー: 指定されたファイルが見つかりません。");
		} catch (IOException e) {
			System.err.println("エラー: ファイルの読み取り中にエラーが発生しました。");
		}
	}

	/**
 	* 入力ストリームからデータを読み取り、第一正規化して出力
	*
	* @param input 第一正規化するデータが含まれる入力ストリーム
	* @throws IOException 入力ストリームの読み取り中にエラーが発生した場合にスローされる
	*/
	private static void normalize(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;

		// 行ごとに第一正規化を行い、出力
		while ((line = reader.readLine()) != null) {
			if (line.trim().isEmpty()) continue; // 空行をスキップ

			// タブで列を分割
			String[] cells = line.split("\t");

			List<String[]> normalizedRows = new ArrayList<>(); // 第一正規化後の行データ
			normalizedRows.add(new String[cells.length]); // 後にループするために列数分の空の配列を追加

			// 第一正規化を行う
			// セルが複数の値を持つ場合は、分解して新しい行を作成
			for (int i = 0; i < cells.length; i++) {
				String[] values = cells[i].split(":"); // 各セルを：で分割

				List<String[]> newRows = new ArrayList<>(); // 生成された新しい行データ

				// 既存の行に次のセルの値を追加
				for (String[] row : normalizedRows) {
					// 既存の行データをコピー.新しい値を追加・新しい行を生成
					for (String value : values) {
						String[] newRow = Arrays.copyOf(row, row.length);
						newRow[i] = value;
						newRows.add(newRow);
					}
				}
				// 新しい行リストをnormalizedRowsに置き換え
				normalizedRows = newRows;
			}

			// 第一正規化したデータを出力
			for (String[] row : normalizedRows) {
				System.out.println(String.join("\t", row)); // 列はタブで区切る
			}
		}
	}
}