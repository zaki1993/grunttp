package com.zaki.grunttp.util.exceptions.runtime;

import com.zaki.grunttp.constant.ServerStatus;

public class ServerRuntimeException extends RuntimeException {

		private final ServerStatus STATUS;

		public ServerRuntimeException(ServerStatus status) {
			super();
			this.STATUS = status;
		}

	    public ServerRuntimeException() {
	        this(ServerStatus.RECOVERABLE);
	    }

	    public ServerRuntimeException(Throwable cause) {
	        this(cause, ServerStatus.RECOVERABLE);
		}

		public ServerRuntimeException(Throwable cause, ServerStatus status) {
		    super(cause);
		    this.STATUS = status;
        }
		public ServerStatus getServerStatus() {
	    	return STATUS;
		}
}
