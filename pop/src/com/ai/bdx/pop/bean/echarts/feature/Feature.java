package com.ai.bdx.pop.bean.echarts.feature;


public class Feature {
	
	private Mark mark;
	private DataZoom dataZoom;
	private DataView dataView;
	private MagicType magicType;
	private Restore restore;
	private SaveAsImage saveAsImage;
	
	public Mark newMark(){
		Mark o = new Mark();
		this.setMark(o);
		return o;
	}
	
	public DataZoom newDataZoom(){
		DataZoom o = new DataZoom();
		this.setDataZoom(o);
		return o;
	}
	
	public DataView newDataView(){
		DataView o = new DataView();
		this.setDataView(o);
		return o;
	}
	
	public MagicType newMagicType(){
		MagicType o = new MagicType();
		this.setMagicType(o);
		return o;
	}
	
	public Restore newRestore(){
		Restore o = new Restore();
		this.setRestore(o);
		return o;
	}
	
	public SaveAsImage newSaveAsImage(){
		SaveAsImage o = new SaveAsImage();
		this.setSaveAsImage(o);
		return o;
	}
	
	public Mark getMark() {
		return mark;
	}
	public Feature setMark(Mark mark) {
		this.mark = mark;
		return this;
	}
	public DataZoom getDataZoom() {
		return dataZoom;
	}
	public Feature setDataZoom(DataZoom dataZoom) {
		this.dataZoom = dataZoom;
		return this;
	}
	public DataView getDataView() {
		return dataView;
	}
	public Feature setDataView(DataView dataView) {
		this.dataView = dataView;
		return this;
	}
	public MagicType getMagicType() {
		return magicType;
	}
	public Feature setMagicType(MagicType magicType) {
		this.magicType = magicType;
		return this;
	}
	public Restore getRestore() {
		return restore;
	}
	public Feature setRestore(Restore restore) {
		this.restore = restore;
		return this;
	}
	public SaveAsImage getSaveAsImage() {
		return saveAsImage;
	}
	public Feature setSaveAsImage(SaveAsImage saveAsImage) {
		this.saveAsImage = saveAsImage;
		return this;
	}
	
}
