package nmt.turkistikyerler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandListViewAdapter extends BaseExpandableListAdapter {


	public List<String> list_parent;
	public HashMap<String, List<String>> list_child;
	public Context context;
	public TextView txt;
	public TextView txt_child;
	public LayoutInflater inflater;
	public ArrayList<Object> item = new ArrayList<Object>();
	@Override
	public int getGroupCount() {
		
		return list_parent.size();
	}
	
	public ExpandListViewAdapter(Context context, List<String> list_parent,HashMap<String, List<String>> list_child)
	{
		this.context = context;
		this.list_parent = list_parent;
		this.list_child = list_child;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		
		return list_child.get(list_parent.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return list_parent.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		return list_child.get(list_parent.get(groupPosition)).get(childPosition);
		
	}

	@Override
	public long getGroupId(int groupPosition) {
		
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View view, ViewGroup parent) {
		String title_name = (String)getGroup(groupPosition);

		if(view == null)
		{
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_header,null);
			view.setBackgroundColor(Color.parseColor("#5F9EA0"));
		}

		txt = (TextView)view.findViewById(R.id.txt_parent);
		txt.setText(title_name);
		
		return view;
	}


	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		
		String txt_child_name = (String)getChild(groupPosition, childPosition);
		if(view==null)
		{
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_child, null);
		}
		if(getGroup(groupPosition).toString().equals("GEZİLECEK YERLER"))
		{
			view.setBackgroundColor(Color.blue(125));
		}
		else if(getGroup(groupPosition).toString().equals("OTELLER"))
		{
			view.setBackgroundColor(Color.blue(125));
		}
		else if(getGroup(groupPosition).toString().equals("MEKANLAR"))
		{
			view.setBackgroundColor(Color.blue(125));
		}
		txt_child = (TextView)view.findViewById(R.id.txt_items);
		txt_child.setText(txt_child_name);
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}
	
	

}
