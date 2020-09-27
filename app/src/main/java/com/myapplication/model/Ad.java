package com.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Ad{

	@SerializedName("company")
	private String company;

	@SerializedName("text")
	private String text;

	@SerializedName("url")
	private String url;

	public void setCompany(String company){
		this.company = company;
	}

	public String getCompany(){
		return company;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"Ad{" + 
			"company = '" + company + '\'' + 
			",text = '" + text + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}