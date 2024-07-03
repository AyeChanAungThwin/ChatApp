package io.github.ayechanaungthwin.chat.cor;

public abstract class BaseHandler implements Handler {

	protected Handler successor;

	@Override
	public void setSuccessor(Handler handler) {
		// TODO Auto-generated method stub
		this.successor = handler;
	}
}
