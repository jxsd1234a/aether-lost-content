package com.legacy.lostaether;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LostSplashes
{
	public final String splash;

	public LostSplashes(String splash)
	{
		this.splash = splash;
	}

	@Override
	public String toString()
	{
		return this.splash;
	}

	public static class Splashes
	{
		private List<String> splashes = new ArrayList<>();

		public List<String> getSplashes()
		{
			return this.splashes;
		}

		@Override
		public String toString()
		{
			String output = "[";
			for (int i = 0; i < this.splashes.size(); i++)
				output.concat(this.splashes.get(i).toString() + (i < this.splashes.size() - 1 ? ", " : ""));
			output.concat("]");
			return output;
		}
	}

	public static class GetSplashesThread extends Thread
	{
		public GetSplashesThread()
		{
			super("Lost Content splashes thread");
			this.setDaemon(true);
		}

		@Override
		public void run()
		{
			try
			{
				String address = "https://moddinglegacy.com/supporters-changelogs/lost-splashes.txt";
				LostAetherContent.LOGGER.debug("Attempting to load the Lost Content splash text list from " + address);
				List<String> splashes = new ArrayList<>();
				URL url = new URL(address);
				HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
				httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

				BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));

				String line;
				while ((line = reader.readLine()) != null)
					splashes.add(line);
				reader.close();

				loadSplashes(splashes);

				LostAetherContent.LOGGER.debug("Successfully loaded the Lost Content splashes list.");
			}
			catch (IOException e)
			{
				LostAetherContent.LOGGER.debug("Couldn't load the Lost Content splashes list. You may be offline or our website could be having issues. Using a default for now.");
				e.printStackTrace();
			}
			catch (Exception e)
			{
				LostAetherContent.LOGGER.debug("Failed to load the Lost Content splashes list. Using a default for now.");
				e.printStackTrace();
			}
		}

		private void loadSplashes(List<String> supporters)
		{
			LostAetherContent.SPLASHES.getSplashes().clear();
			LostAetherContent.SPLASHES.getSplashes().addAll(supporters);
		}
	}
}