package CiLin;

public class Test {
	public static void main(String args[]) {
		String word1 = "风景", word2 = "景色";
		double sim = 0;
		sim = CiLin.calcWordsSimilarity(word1, word2);//计算两个词的相似度
		System.out.println(word1 + "  " + word2 + "的相似度为：" + sim);
	}
}
