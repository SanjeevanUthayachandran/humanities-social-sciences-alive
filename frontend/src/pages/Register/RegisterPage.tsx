import { Eye, EyeOff, Lock, Mail, UserRound } from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import GoogleAuthButton from "@/components/auth/GoogleAuthButton";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { getAuthenticationDestination, register, storeAuthentication } from "@/services/authService";
import { getRequestErrorMessage } from "@/services/requestError";
import type { AuthenticationData } from "@/types/api";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function completeGoogleRegistration(authentication: AuthenticationData) {
    storeAuthentication(authentication, false);
    navigate(getAuthenticationDestination(authentication));
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);
    const form = new FormData(event.currentTarget);
    const email = String(form.get("email") ?? "").trim();

    try {
      const message = await register({
        firstName: String(form.get("firstName") ?? ""),
        lastName: String(form.get("lastName") ?? ""),
        email,
        password: String(form.get("password") ?? ""),
      });
      navigate("/login", {
        replace: true,
        state: {
          registrationSuccess: message || "Account created successfully. You can now log in.",
          email,
        },
      });
    } catch (requestError) {
      setError(getRequestErrorMessage(requestError, "Registration failed."));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="bg-slate-50 px-4 py-12 sm:py-20">
      <Container className="max-w-3xl">
        <form onSubmit={handleSubmit} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-[0_24px_60px_-32px_rgba(15,23,42,0.35)] sm:p-10">
            <div className="text-center">
              <img src="/images/hssa-logo.svg" alt="" aria-hidden="true" className="mx-auto size-16 rounded-full ring-1 ring-slate-200" />
              <p className="mt-5 text-xs font-bold uppercase tracking-[0.2em] text-orange-700">Join HSSA</p>
              <h1 className="mt-3 text-3xl font-bold tracking-tight text-slate-950">Create your learner account</h1>
              <p className="mt-2 text-sm leading-6 text-slate-600">Start learning, researching, and participating in the HSSA community.</p>
            </div>
            {error ? <p role="alert" className="mt-6 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm font-semibold text-red-700">{error}</p> : null}
            <div className="mt-8 grid gap-5 sm:grid-cols-2">
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">First name</span>
                <span className="relative mt-2 block">
                  <UserRound className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="firstName" required autoComplete="given-name" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </span>
              </label>
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">Last name</span>
                <input name="lastName" required autoComplete="family-name" className="mt-2 h-12 w-full rounded-md border border-slate-200 px-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
              </label>
              <label className="block sm:col-span-2">
                <span className="text-sm font-semibold text-slate-700">Email</span>
                <span className="relative mt-2 block">
                  <Mail className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="email" required type="email" autoComplete="email" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </span>
              </label>
              <label className="block sm:col-span-2">
                <span className="text-sm font-semibold text-slate-700">Password</span>
                <span className="relative mt-2 block">
                  <Lock className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input name="password" required minLength={8} type={showPassword ? "text" : "password"} autoComplete="new-password" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-12 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
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
                <p className="mt-2 text-xs text-slate-500">Use at least 8 characters.</p>
              </label>
            </div>
            <div className="mt-6">
              <Button type="submit" disabled={isSubmitting} className="h-12 w-full bg-blue-600 hover:bg-blue-700">
                {isSubmitting ? "Creating account..." : "Create Account"}
              </Button>
            </div>
            <div className="my-6 flex items-center gap-4" aria-hidden="true">
              <span className="h-px flex-1 bg-slate-200" />
              <span className="text-xs font-semibold uppercase tracking-[0.16em] text-slate-400">or continue with</span>
              <span className="h-px flex-1 bg-slate-200" />
            </div>
            <GoogleAuthButton
              mode="register"
              onAuthenticated={completeGoogleRegistration}
              onError={setError}
            />
            <p className="mt-6 text-center text-sm text-slate-600">
              Already have an account?{" "}
              <Link to="/login" className="font-semibold text-blue-700 hover:text-blue-800">
                Login
              </Link>
            </p>
        </form>
      </Container>
    </section>
  );
}
