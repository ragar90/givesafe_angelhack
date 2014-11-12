package co.foodcircles.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import co.foodcircles.R;
import co.foodcircles.exception.NetException2;
import co.foodcircles.net.Net;
import co.foodcircles.util.AndroidUtils;
import co.foodcircles.util.FontSetter;
import co.foodcircles.util.FoodCirclesApplication;
import co.foodcircles.util.FoodCirclesUtils;
import co.foodcircles.util.TwitterLogin;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class SignInActivity extends Activity {
	EditText email;
	EditText password;
	Button signInButton;
	TextView signUpButton;
	private Button buttonFacebook;
	private Button buttonTwitter;
	SimpleFacebook mSimpleFacebook;
	boolean signedIn=false;
	Context mcontext;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signin);
		FontSetter.overrideFonts(this, findViewById(R.id.root));
		TextView t2 = (TextView) findViewById(R.id.forgotpassword);
	    t2.setMovementMethod(LinkMovementMethod.getInstance());
		mcontext = this;
		email = (EditText) findViewById(R.id.editTextEmail);
		password = (EditText) findViewById(R.id.editTextPassword);
		signInButton = (Button) findViewById(R.id.buttonSignIn);
		signUpButton = (TextView) findViewById(R.id.buttonSignUp);
		buttonFacebook = (Button) findViewById(R.id.buttonFacebookI);
		buttonTwitter = (Button) findViewById(R.id.buttonTwitterI);
		buttonFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mSimpleFacebook.isLogin()) {
					mSimpleFacebook.getProfile(mProfileListener); 
				} else {
					mSimpleFacebook.login(mOnLoginListener);
				}
			}
		});
		buttonTwitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new TwitterLogin(mcontext).twitterSignUp();
			}
		});

		if (FoodCirclesUtils.getPassword(this).isEmpty() == false) {
			password.setText(FoodCirclesUtils.getPassword(this));
			new Thread() {
				public void run() {
					signIn(email.getText().toString(), password.getText().toString());
				};
			}.start();
		}

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						signIn(email.getText().toString(), password.getText().toString());
					};
				}.start();
			}
		});

		signUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
				startActivity(intent);
				SignInActivity.this.finish();
			}
		});
		
	}

	private void signIn(final String email, final String password) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AndroidUtils.showProgress(SignInActivity.this);
			}
		});
		try {
			final String token = Net.signIn(email, password);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					FoodCirclesUtils.saveToken(SignInActivity.this, token);
					FoodCirclesUtils.saveEmail(SignInActivity.this, email);
					FoodCirclesUtils.savePassword(SignInActivity.this, password);
					AndroidUtils.dismissProgress();
					gotToSignedInPage();
				}
			});
		} catch (final NetException2 e) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AndroidUtils.alert(SignInActivity.this, "Oops! Sign-in Failed : " + e.getMessage());
					AndroidUtils.dismissProgress();
				}
			});
		}
	}

	private void gotToSignedInPage() {
		Intent intent = new Intent(SignInActivity.this, MainActivity.class);
		intent.putExtra("tab", 1);
		startActivity(intent);
		SignInActivity.this.finish();
		FoodCirclesApplication app = (FoodCirclesApplication) getApplicationContext();
		app.newTop();
	}

	private void fbSignIn(final String userID,final String emailid) {
		AndroidUtils.showProgress(SignInActivity.this);
		new Thread() {
			public void run() {
				try {
					final String token = Net.facebookSignUp(userID,emailid);
					SignInActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AndroidUtils.dismissProgress();
							
							FoodCirclesUtils.saveToken(SignInActivity.this,
									token);
							Intent intent = new Intent(SignInActivity.this,
									MainActivity.class);
							intent.putExtra("tab", 1);
							
							startActivity(intent);
							SignInActivity.this.finish();
						}
					});
				} catch (final NetException2 e) {
					SignInActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AndroidUtils.showAlertOk(SignInActivity.this,
									"Sign-in Failed - " + e.getMessage());
							AndroidUtils.dismissProgress();
						}
					});
				}
			};
		}.start();
	}

	 @Override
	 protected void onResume() {
	 	super.onResume();
	 	mSimpleFacebook = SimpleFacebook.getInstance(this);
	 }


	private OnLoginListener mOnLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			Toast.makeText(mcontext, "Facebook Login Failed:" + reason, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onException(Throwable throwable) {
			Toast.makeText(mcontext, "Whoops- we've encountered a problem!", Toast.LENGTH_SHORT).show();
			throwable.printStackTrace();
		}

		@Override
		public void onThinking() {
			// Place here if we want to show progress bar while login is occurring
		}

		@Override
		public void onLogin() {
			mSimpleFacebook.getProfile(mProfileListener); 
		}

		@Override
		public void onNotAcceptingPermissions(Permission.Type type) {
			Toast.makeText(mcontext,"Facebook permissions cancelled!", Toast.LENGTH_SHORT).show();
		}
	};
	private OnProfileListener mProfileListener = new OnProfileListener() {
		@Override
		public void onComplete(Profile profile) {
			final String email = profile.getEmail();
			final String id = profile.getId();
			runOnUiThread(new Runnable() {
				@Override
				public void run() { fbSignIn(id , email); }
			});
		}
	};
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
