package co.foodcircles.activities;

import java.net.URI;
import java.util.Map;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import co.foodcircles.R;
import co.foodcircles.util.Utils;

public class TwitterWebviewActivity extends Activity {

	Twitter twitter;
    RequestToken requestToken;
    public final static String consumerKey = "wrAcrs7txBouXgfeCt5QQ"; 
    public final static String consumerSecret = "utJm01GSuqa3LQyNyoLrf9EguyswmGIJBqQblQ4nCs";
    private final String CALLBACKURL = "http://foodcircles.net/?source=twitter&device=android";  //Callback URL that tells the WebView to load this activity when it finishes with twitter.com. (see manifest)
	private WebView webView;
	private ProgressBar progressBar;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitterwebview);
		webView=(WebView) findViewById(R.id.webView);
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        return false;
		    }
			@Override
			public void onLoadResource(WebView view, String url) {
				
				String token=getAccessTokenFromCallBackUrl(requestToken, CALLBACKURL, url);
				if(token!=null){
					TwitterWebviewActivity.this.onReceiveToken(token);
				}
				super.onLoadResource(view, url);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		    @Override
		    public void onReceivedError (WebView view, int errorCode, 
		        String description, String failingUrl) {
		    }
		});
		
		webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
               if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
            	   progressBar.setVisibility(ProgressBar.VISIBLE);
               }
               progressBar.setProgress(progress);
               if(progress == 100) {
            	   progressBar.setVisibility(ProgressBar.GONE);
               }
            }
        });
		getTwitterAuthTokenViaWebView(consumerKey,consumerSecret);
	}
	
	protected void onReceiveToken(String token) {
		Intent intent = new Intent(TwitterWebviewActivity.this, MainActivity.class);
		intent.putExtra("tab", 1);
		startActivity(intent);
		TwitterWebviewActivity.this.finish();
	}

	private void openLinkByWebView(String url){
		webView.stopLoading();
		webView.loadUrl(url);
	}

	public void getTwitterAuthTokenViaWebView(String consumerKey,String consumerSecret) {
        try {
            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(consumerKey, consumerSecret);
            requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
            String authUrl = requestToken.getAuthenticationURL();
            openLinkByWebView(authUrl);
        } catch (TwitterException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("in Main.OAuthLogin", ex.getMessage());
        }
    }

	@SuppressWarnings("unused")
	private  String getAccessTokenFromCallBackUrl(RequestToken requestToken,String beforeCallBackUrl,String afterCallBackUrl){
		URI afterCallBackUrlUri=URI.create(afterCallBackUrl);
		URI beforeCallBackUrlUri=URI.create(beforeCallBackUrl);
		if(afterCallBackUrlUri.getHost().equals(beforeCallBackUrlUri.getHost())){
			String query = afterCallBackUrlUri.getQuery();
			if(query==null){
				return null;
			}
			Map<String, String> map = Utils.convertQueryToMap(afterCallBackUrlUri.getQuery());
			if(map.containsKey("oauth_verifier")){
				
				String verifier = map.get("oauth_verifier");
                AccessToken accessToken;
				try {
					accessToken = twitter.getOAuthAccessToken(requestToken,verifier);
                    String token = accessToken.getToken();
                    String secret = accessToken.getTokenSecret();
                    String name=accessToken.getScreenName();
    				return map.get("oauth_token");				
				} catch (TwitterException e) {
					return null;
				}
			}else{
				return null;				
			}
		}else{
			return null;
		}
	}
}