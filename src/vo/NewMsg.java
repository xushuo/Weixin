package vo;

import java.util.List;

public class NewMsg extends BaseMsg{
	
	/*
	 * ArticleCount	是	图文消息个数，限制为10条以内
		Articles	是	多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
	 * 
	 * */
	private int ArticleCount;
	private List<News> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
	
}
