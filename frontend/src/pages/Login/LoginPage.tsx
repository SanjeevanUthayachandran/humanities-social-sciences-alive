import { Eye, Globe, Lock, Mail } from "lucide-react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import { Button } from "@/components/ui/button";

export default function LoginPage() {
  return (
    <>
      <PageBanner
        eyebrow="Login"
        title="Access your future learning portal."
        description="This professional login interface is UI-only and prepared for JWT, refresh tokens, Google login, and role-based access later."
      />

      <section className="py-16 sm:py-20">
        <Container className="max-w-3xl">
          <form className="rounded-lg border border-slate-200 bg-white p-6 shadow-xl">
            <div className="text-center">
              <h2 className="text-2xl font-bold text-slate-950">Welcome back</h2>
              <p className="mt-2 text-sm text-slate-600">Sign in to continue to Humanities & Social Sciences Alive.</p>
            </div>
            <div className="mt-8 space-y-5">
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">Email</span>
                <span className="relative mt-2 block">
                  <Mail className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input type="email" className="h-12 w-full rounded-md border border-slate-200 pl-12 pr-4 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                </span>
              </label>
              <label className="block">
                <span className="text-sm font-semibold text-slate-700">Password</span>
                <span className="relative mt-2 block">
                  <Lock className="pointer-events-none absolute left-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                  <input type="password" className="h-12 w-full rounded-md border border-slate-200 px-12 text-sm outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-100" />
                  <Eye className="pointer-events-none absolute right-4 top-1/2 size-5 -translate-y-1/2 text-slate-400" />
                </span>
              </label>
              <div className="flex flex-col gap-3 text-sm sm:flex-row sm:items-center sm:justify-between">
                <label className="flex items-center gap-2 font-medium text-slate-700">
                  <input type="checkbox" className="size-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500" />
                  Remember me
                </label>
                <a href="#" className="font-semibold text-blue-700 hover:text-blue-800">
                  Forgot password?
                </a>
              </div>
              <Button type="button" className="h-12 w-full bg-blue-600 hover:bg-blue-700">
                Login
              </Button>
              <Button type="button" variant="outline" className="h-12 w-full">
                <Globe />
                Continue with Google
              </Button>
            </div>
            <p className="mt-6 text-center text-sm text-slate-600">
              New to HSSA?{" "}
              <Link to="/register" className="font-semibold text-blue-700 hover:text-blue-800">
                Create an account
              </Link>
            </p>
          </form>
        </Container>
      </section>
    </>
  );
}
