import { LayoutDashboard, LogOut, Menu, Search, UserRound, X } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, NavLink } from "react-router-dom";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { NAVIGATION } from "@/constants/navigation";
import { cn } from "@/lib/utils";
import { clearAuthentication, getStoredUser } from "@/services/authService";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const [user, setUser] = useState(getStoredUser);
  const workspacePath = user?.roles.includes("ADMIN") ? "/admin" : user?.roles.includes("INSTRUCTOR") ? "/instructor" : null;

  useEffect(() => {
    const update = () => setUser(getStoredUser());
    window.addEventListener("hssa-auth-changed", update);
    window.addEventListener("storage", update);
    return () => {
      window.removeEventListener("hssa-auth-changed", update);
      window.removeEventListener("storage", update);
    };
  }, []);

  return (
    <header className="sticky top-0 z-50 border-b border-slate-200 bg-slate-50/95 backdrop-blur">
      <Container>
        <div className="flex h-[72px] items-center justify-between gap-4">
          <Link
            to="/"
            className="flex min-w-0 items-center gap-3"
            aria-label="Humanities & Social Sciences Alive home"
          >
            <img
              src="/images/hssa-logo.svg"
              alt=""
              aria-hidden="true"
              className="size-11 shrink-0 rounded-full"
            />
            <span className="min-w-0 font-heading text-[0.95rem] font-semibold leading-tight text-slate-950">
              Humanities & Social
              <span className="block text-blue-600">Sciences Alive</span>
            </span>
          </Link>

          <nav className="hidden items-center gap-1 xl:flex" aria-label="Main navigation">
            {NAVIGATION.map((item) => (
              <NavLink
                key={item.name}
                to={item.path}
                className={({ isActive }) =>
                  cn(
                    "rounded-full px-3 py-2 text-sm font-medium text-slate-700 transition hover:bg-blue-100 hover:text-blue-700",
                    isActive && "bg-blue-100 font-semibold text-blue-700",
                  )
                }
              >
                {item.name}
              </NavLink>
            ))}
          </nav>

          <div className="hidden items-center gap-2 xl:flex">
            <Link
              to="/search"
              aria-label="Search"
              className={buttonVariants({ variant: "ghost", size: "icon", className: "text-slate-700 hover:bg-blue-100 hover:text-blue-700" })}
              onClick={() => setIsOpen(false)}
            >
              <Search />
            </Link>
            {user ? <>
              <Link to="/account" className={buttonVariants({ variant: "ghost", className: "text-slate-800 hover:bg-blue-100" })}><UserRound />My account</Link>
              {workspacePath ? <Link to={workspacePath} className={buttonVariants({ variant: "outline", className: "border-slate-300 text-slate-900 hover:bg-blue-100" })}><LayoutDashboard />Workspace</Link> : null}
              <Button type="button" onClick={clearAuthentication} className="bg-blue-600 text-white hover:bg-blue-700"><LogOut />Logout</Button>
            </> : <><Link to="/login" className={buttonVariants({ variant: "outline", className: "border-slate-300 text-slate-900 hover:bg-blue-100" })}>Login</Link><Link to="/register" className={buttonVariants({ className: "bg-blue-600 text-white hover:bg-blue-700" })}>Register</Link></>}
          </div>

          <div className="flex items-center gap-2 xl:hidden">
            <Link
              to="/search"
              aria-label="Search"
              className={buttonVariants({ variant: "ghost", size: "icon", className: "text-slate-700 hover:bg-blue-100 hover:text-blue-700" })}
            >
              <Search />
            </Link>
            <Button
              type="button"
              variant="outline"
              size="icon"
              className="border-slate-300 text-slate-800 hover:bg-blue-100"
              aria-label={isOpen ? "Close navigation menu" : "Open navigation menu"}
              aria-expanded={isOpen}
              onClick={() => setIsOpen((current) => !current)}
            >
              {isOpen ? <X /> : <Menu />}
            </Button>
          </div>
        </div>
      </Container>

      {isOpen ? (
        <div className="border-t border-slate-200 bg-slate-50 xl:hidden">
          <Container className="py-4">
            <nav className="grid gap-1" aria-label="Mobile navigation">
              {NAVIGATION.map((item) => (
                <NavLink
                  key={item.name}
                  to={item.path}
                  onClick={() => setIsOpen(false)}
                  className={({ isActive }) =>
                    cn(
                      "rounded-xl px-3 py-3 text-sm font-medium text-slate-800 transition hover:bg-blue-100 hover:text-blue-700",
                      isActive && "bg-blue-100 font-semibold text-blue-700",
                    )
                  }
                >
                  {item.name}
                </NavLink>
              ))}
            </nav>
            <div className="mt-4 grid gap-3 sm:grid-cols-2">
              {user ? <>
                <Link to="/account" onClick={() => setIsOpen(false)} className={buttonVariants({ variant: "outline", className: "w-full border-slate-300 text-slate-900 hover:bg-blue-100" })}>My account</Link>
                {workspacePath ? <Link to={workspacePath} onClick={() => setIsOpen(false)} className={buttonVariants({ variant: "outline", className: "w-full border-slate-300 text-slate-900 hover:bg-blue-100" })}>Workspace</Link> : null}
                <Button type="button" onClick={() => { clearAuthentication(); setIsOpen(false); }} className="w-full bg-blue-600 text-white hover:bg-blue-700">Logout</Button>
              </> : <>
              <Link
                to="/login"
                onClick={() => setIsOpen(false)}
                className={buttonVariants({ variant: "outline", className: "w-full border-slate-300 text-slate-900 hover:bg-blue-100" })}
              >
                Login
              </Link>
              <Link
                to="/register"
                onClick={() => setIsOpen(false)}
                className={buttonVariants({ className: "w-full bg-blue-600 text-white hover:bg-blue-700" })}
              >
                Register
              </Link>
              </>}
            </div>
          </Container>
        </div>
      ) : null}
    </header>
  );
}
