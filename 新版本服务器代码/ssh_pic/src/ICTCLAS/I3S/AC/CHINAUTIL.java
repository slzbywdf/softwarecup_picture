package ICTCLAS.I3S.AC;

public class CHINAUTIL {
	public static String testICTCLAS_ParagraphProcess(String sInput)
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			//分词所需库路径
			String argu = "D:\\\\pic_configure\\";
			//初始化
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("UTF-8")) == false)
			{
				System.out.println("Init Fail!");
				return null;
			}
			//设置词性标注集（2北大二级标注集）
			testICTCLAS50.ICTCLAS_SetPOSmap(2);
			byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("UTF-8"), 0, 1);//分词处理
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
			//System.out.println(nativeStr);
			//释放分词组件资源
			testICTCLAS50.ICTCLAS_Exit();
			return nativeStr;
		}
		catch (Exception ex)
		{
		}
		return null;
	}

}
