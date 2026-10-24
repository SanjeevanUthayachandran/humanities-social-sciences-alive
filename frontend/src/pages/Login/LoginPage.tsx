import { CheckCircle2, Eye, EyeOff, Lock, Mail } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import GoogleAuthButton from "@/components/auth/GoogleAuthButton";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { getAuthenticationDestination, login, storeAuthentication } from "@/services/authService";
import type { AuthenticationData } from "@/types/api";
import { getRequestErrorMessage } from "@/services/requestError";

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const registrationState = location.state as { registrationSuccess?: string; email?: string } | null;
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function completeAuthentication(authentication: AuthenticationData) {
    storeAuthentication(authentication, false);
    navigate(getAuthenticationDestination(authentication));
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);
    const form = new FormData(event.currentTarget);

    try {
      const authentication = await login({
        email: String(form.get("email") ?? ""),
        password: String(form.get("password") ?? ""),
      });
      storeAuthentication(authentication, form.get("remember") === "on");
      navigate(getAuthenticationDestination(authentication));
    } catch (requestError) {
      setError(getRequestErrorMessage(requestError, "Login failed. Check your email and password."));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="bg-slate-50 px-4 py-12 sm:py-20">
      <Container className="max-w-2xl">
        <form onSubmit={handleSubmit} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-[0_24px_60px_-32px_rgba(15,23,42,0.35)] sm:p-10">
          <div className="text-center">
            <img src="/images/hssa-logo.svg" alt="" aria-hidden="true" className="mx-auto size-16 rounded-full ring-1 ring-slate-200" />
            <p className="mt-5 text-xs font-bold uppercase tracking-[0.2em] text-orange-700">Welcome back</p>
            <h1 className="mt-3 text-3xl font-bold tracking-tight text-slate-950">Sign in to your HSSA account</h1>
            <p className="mt-2 text-sm leading-6 text-slate-600">Continue your learning, research, and community journey.</p>
          </div>
            {registrationState?.registrationSuccess ? (
              <div role="status" className="mt-6 flex items-start gap-3 rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm font-semibold text-emerald-800">
                <CheckCircle2 className="mt-0.5 size-5 shrink-0" aria-hidden="true" />
                <span>{registrationState.registrationSuccess}</span>
              </div>
            ) : null}
            {error ? <p role="alert" className="mt-6 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm font-semibold text-red-700">{error}</p> : null}
            <div className="mt-8 space-y-5">
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">Email</span>
                <span className="relative mt-2 block">
                  <Mail className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="email" required type="email" autoComplete="email" defaultValue={registrationState?.email ?? ""} className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </span>
              </label>
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">Password</span>
                <span className="relative mt-2 block">
                  <Lock className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="password" required type={showPassword ? "text" : "password"} autoComplete="current-password" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-12 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                  <button
                    type="button"
                    onClick={() => setShowPassword((current) => !current)}
                    className="absolute right-3 top-1/2 flex size-8 -translate-y-1/2 items-center justify-center rounded-md text-slate-500 transition hover:bg-slate-100 hover:text-slate-800 focus:outline-none focus:ring-2 focus:ring-blue-200"
                    aria-label={showPassword ? "Hide password" : "Show password"}
                    aria-pressed={showPassword}
                  >
                    {showPassword ? <EyeOff className="size-5" /> : <Eye className="size-5" />}
                  </button>
                </span>
              </label>
              <div className="flex flex-col gap-3 text-sm sm:flex-row sm:items-center sm:justify-between">
                <label className="flex items-center gap-2 font-medium text-slate-700">
                  <input name="remember" type="checkbox" className="size-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500" />
                  Remember me
                </label>
                <Link to="/forgot-password" className="font-semibold text-blue-700 hover:text-blue-800">
                  Forgot password?
                </Link>
              </div>
              <Button type="submit" disabled={isSubmitting} className="h-12 w-full bg-blue-600 hover:bg-blue-700">
                {isSubmitting ? "Signing in..." : "Login"}
              </Button>
            </div>
            <div className="my-6 flex items-center gap-4" aria-hidden="true">
              <span className="h-px flex-1 bg-slate-200" />
              <span className="text-xs font-semibold uppercase tracking-[0.16em] text-slate-400">or continue with</span>
              <span className="h-px flex-1 bg-slate-200" />
            </div>
            <GoogleAuthButton
              mode="login"
              onAuthenticated={completeAuthentication}
              onError={setError}
            />
            <p className="mt-6 text-center text-sm text-slate-600">
              New to HSSA?{" "}
              <Link to="/register" className="font-semibold text-blue-700 hover:text-blue-800">
                Create an account
              </Link>
            </p>
        </form>
      </Container>
    </section>
  );
}
