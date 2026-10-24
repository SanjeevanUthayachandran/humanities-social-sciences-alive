import { useEffect, useRef, useState } from "react";
import type { AuthenticationData } from "@/types/api";
import { loginWithGoogle } from "@/services/authService";
import { getRequestErrorMessage } from "@/services/requestError";

const GOOGLE_SCRIPT_ID = "google-identity-services";
const GOOGLE_SCRIPT_URL = "https://accounts.google.com/gsi/client";

interface GoogleAuthButtonProps {
  mode: "login" | "register";
  onAuthenticated: (authentication: AuthenticationData) => void;
  onError: (message: string) => void;
}

function GoogleMark() {
  return (
    <svg aria-hidden="true" viewBox="0 0 24 24" className="size-5">
      <path fill="#4285F4" d="M21.6 12.23c0-.71-.06-1.4-.18-2.07H12v3.92h5.38a4.6 4.6 0 0 1-2 3.02v2.54h3.24c1.9-1.75 2.98-4.33 2.98-7.41Z" />
      <path fill="#34A853" d="M12 22c2.7 0 4.98-.9 6.63-2.43l-3.24-2.54c-.9.6-2.05.96-3.39.96-2.61 0-4.82-1.76-5.61-4.13H3.04v2.62A10 10 0 0 0 12 22Z" />
      <path fill="#FBBC05" d="M6.39 13.86A6.02 6.02 0 0 1 6.08 12c0-.65.11-1.28.31-1.86V7.52H3.04A10 10 0 0 0 2 12c0 1.61.38 3.14 1.04 4.48l3.35-2.62Z" />
      <path fill="#EA4335" d="M12 6.01c1.47 0 2.79.51 3.83 1.5l2.87-2.88A9.62 9.62 0 0 0 12 2a10 10 0 0 0-8.96 5.52l3.35 2.62C7.18 7.77 9.39 6.01 12 6.01Z" />
    </svg>
  );
}

export default function GoogleAuthButton({ mode, onAuthenticated, onError }: GoogleAuthButtonProps) {
  const configuredClientId = (import.meta.env.VITE_GOOGLE_CLIENT_ID as string | undefined)?.trim() ?? "";
  const clientId = /^\d+-[a-z0-9_-]+\.apps\.googleusercontent\.com$/i.test(configuredClientId)
    ? configuredClientId
    : "";
  const buttonContainerRef = useRef<HTMLDivElement>(null);
  const callbacksRef = useRef({ onAuthenticated, onError });
  const [isBusy, setIsBusy] = useState(false);

  useEffect(() => {
    callbacksRef.current = { onAuthenticated, onError };
  }, [onAuthenticated, onError]);

  useEffect(() => {
    if (!clientId) return;
    let cancelled = false;

    const renderGoogleButton = () => {
      if (cancelled || !window.google || !buttonContainerRef.current) return;
      buttonContainerRef.current.replaceChildren();
      window.google.accounts.id.initialize({
        client_id: clientId,
        ux_mode: "popup",
        callback: async ({ credential }) => {
          setIsBusy(true);
          try {
            callbacksRef.current.onAuthenticated(await loginWithGoogle(credential));
          } catch (requestError) {
            callbacksRef.current.onError(
              getRequestErrorMessage(requestError, "Google sign-in failed. Please try again."),
            );
          } finally {
            setIsBusy(false);
          }
        },
      });
      window.google.accounts.id.renderButton(buttonContainerRef.current, {
        type: "standard",
        theme: "outline",
        size: "large",
        text: mode === "register" ? "signup_with" : "signin_with",
        shape: "rectangular",
        logo_alignment: "left",
        width: Math.min(buttonContainerRef.current.clientWidth || 400, 400),
      });
    };

    if (window.google) {
      renderGoogleButton();
    } else {
      let script = document.getElementById(GOOGLE_SCRIPT_ID) as HTMLScriptElement | null;
      if (!script) {
        script = document.createElement("script");
        script.id = GOOGLE_SCRIPT_ID;
        script.src = GOOGLE_SCRIPT_URL;
        script.async = true;
        script.defer = true;
        document.head.appendChild(script);
      }
      script.addEventListener("load", renderGoogleButton, { once: true });
    }

    return () => {
      cancelled = true;
    };
  }, [clientId, mode]);

  if (!clientId) {
    return (
      <button
        type="button"
        onClick={() => onError("Google sign-in needs a valid OAuth Web Client ID. Add it to frontend/.env and restart the frontend.")}
        className="flex h-11 w-full items-center justify-center gap-3 rounded-md border border-slate-300 bg-white px-4 text-sm font-semibold text-slate-700 transition hover:bg-slate-50 focus:outline-none focus:ring-3 focus:ring-blue-100"
      >
        <GoogleMark />
        {mode === "register" ? "Sign up with Google" : "Sign in with Google"}
      </button>
    );
  }

  return (
    <div className="relative flex min-h-11 w-full justify-center">
      <div ref={buttonContainerRef} className={isBusy ? "pointer-events-none opacity-60" : ""} />
      {isBusy ? <span className="sr-only">Completing Google sign-in...</span> : null}
    </div>
  );
}
