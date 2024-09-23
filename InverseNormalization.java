package com.example.demo;

import java.io.*;
import java.util.*;


/**
 * キーと値の2列からなるTSVデータを読み込み、
 * 同じキーを持つ値をグループ化したTSVデータを出力
 *
 * @param args 逆正規化を行うTSVファイル名
 */
public class InverseNormalization {

	/**
	 * コマンドライン引数として指定されたファイルを読み込み、その内容を逆正規化
	 * @param args 逆正規化を行うTSVファイル名
	 * @throws IOException 入力ストリームの読み取り中にエラーが発生した場合にスローされる
	 */
	public static void main(String[] args) throws IOException {
		// ファイルの指定がなければエラーを表示して終了
		if (args.length == 0) {
			System.err.println("エラー: ファイル名を指定してください。");
			return;
		}

		// 指定されたファイルを入力ストリームに使用
		try (FileInputStream fis = new FileInputStream(new File(args[0]))) {
			inverseNormalize(fis);
		} catch (FileNotFoundException e) {
			System.err.println("エラー: 指定されたファイルが見つかりません。");
		}
    }

	/**
 	* 入力ストリームからデータを読み取り、逆正規化
	*
	* @param input 逆正規化するデータが含まれる入力ストリーム
	* @throws IOException 入力ストリームの読み取り中にエラーが発生した場合にスローされる
	*/
    public static void inverseNormalize(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;

        Map<String, List<String>> groupMap = new HashMap<>(); // 同じセルに入るキーをグループ化するためのマップ

		// ファイルを1行ずつ読み込み、同じセルに入る値（キー）をグループ化
		while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue; // 空行をスキップ

            // 各行をタブで分割
            String[] parts = line.split("\t");
            if (parts.length != 2) continue; // 不正な行をスキップ

            String key = parts[0];
            String value = parts[1];

            // キーでグループ化する
            // まず、キーに対応するリストが存在するかどうかを確認する
			List<String> list = groupMap.get(key);

			// リストが存在しない場合は新しく作成してマップに追加する
			if (list == null) {
				list = new ArrayList<>();
				groupMap.put(key, list);
			}

			// リストに値を追加する
			list.add(value);
        }

        // グループ化した結果を出力
        for (Map.Entry<String, List<String>> entry : groupMap.entrySet()) {
            String key = entry.getKey();
            String joinedValues = String.join(":", entry.getValue());
            System.out.println(key + "\t" + joinedValues);
        }
    }
}
