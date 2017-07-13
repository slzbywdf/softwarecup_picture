package cn.picture.exception;

public class tranException extends RuntimeException
{
	// 定义一个无参数的构造器
	public tranException()
	{
	}
	// 定义一个带message参数的构造参数
	public tranException(String message)
	{
		super(message);
	}
}
