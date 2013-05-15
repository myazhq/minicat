package com.mcxiaoke.fanfouapp.fragments;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import com.mcxiaoke.fanfouapp.api.Paging;
import com.mcxiaoke.fanfouapp.app.AppContext;
import com.mcxiaoke.fanfouapp.controller.DataController;
import com.mcxiaoke.fanfouapp.dao.model.StatusModel;
import com.mcxiaoke.fanfouapp.service.FanFouService;
import com.mcxiaoke.fanfouapp.util.Utils;


/**
 * @author mcxiaoke
 * @version 1.0 2012.02.06
 * @version 1.1 2012.03.08
 * @version 1.2 2012.03.19
 * 
 */
public class MentionTimelineFragment extends BaseTimlineFragment {
	private static final String TAG = MentionTimelineFragment.class
			.getSimpleName();

	public static MentionTimelineFragment newInstance() {
		return newInstance(false);
	}

	public static MentionTimelineFragment newInstance(boolean refresh) {
		Bundle args = new Bundle();
		args.putBoolean("refresh", refresh);
		MentionTimelineFragment fragment = new MentionTimelineFragment();
		fragment.setArguments(args);
		if (AppContext.DEBUG) {
			Log.d(TAG, "newInstance() " + fragment);
		}
		return fragment;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		getActivity().setTitle("@我的消息");
	}

	@Override
	protected int getType() {
		return StatusModel.TYPE_MENTIONS;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return DataController.getTimelineCursorLoader(getActivity(),
				StatusModel.TYPE_MENTIONS);
	}

	@Override
	protected void doFetch(boolean doGetMore) {
		if (AppContext.DEBUG) {
			Log.d(TAG, "doFetch() doGetMore=" + doGetMore);
		}
		final ResultHandler handler = new ResultHandler(this);
		final Cursor cursor = getCursor();
		Paging p = new Paging();
		if (doGetMore) {
			p.maxId = Utils.getMaxId(cursor);
		} else {
			p.sinceId = Utils.getSinceId(cursor);
		}
		if (AppContext.DEBUG) {
			Log.d(TAG, "doFetch() doGetMore=" + doGetMore + " Paging=" + p);
		}
		FanFouService.getTimeline(getActivity(), StatusModel.TYPE_MENTIONS,
				handler, p);
	}

	@Override
	public String getTitle() {
		return "提及";
	}

}