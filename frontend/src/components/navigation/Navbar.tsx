import { Menu, Search, X } from "lucide-react";
import { useState } from "react";
import { Link, NavLink } from "react-router-dom";
import brandImage from "@/assets/hssa-brand.png";
import Container from "@/components/common/Container";
import { Button } from "@/components/ui/button";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { NAVIGATION } from "@/constants/navigation";
import { cn } from "@/lib/utils";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <header className="sticky top-0 z-50 border-b border-slate-800/20 bg-slate-950/95 shadow-lg backdrop-blur">
      <Container>
        <div className="flex h-20 items-center justify-between gap-4">
          <Link
            to="/"
            className="flex min-w-0 items-center gap-3"
            aria-label="Humanities & Social Sciences Alive home"
          >
            <img src={brandImage} alt="" className="size-11 rounded-md object-cover object-left" />
            <span className="hidden min-w-0 text-sm font-bold leading-tight text-slate-100 sm:block">
              Humanities & Social
              <span className="block text-blue-300">Sciences Alive</span>
            </span>
          </Link>

          <nav className="hidden items-center gap-1 lg:flex" aria-label="Main navigation">
            {NAVIGATION.map((item) => (
              <NavLink
                key={item.name}
                to={item.path}
                className={({ isActive }) =>
                  cn(
                    "rounded-md px-3 py-2 text-sm font-semibold text-slate-300 transition hover:bg-slate-800 hover:text-white",
                    isActive && "bg-blue-900/70 text-white",
                  )
                }
              >
                {item.name}
              </NavLink>
            ))}
          </nav>

          <div className="hidden items-center gap-2 lg:flex">
            <Link
              to="/search"
              aria-label="Search"
              className={buttonVariants({ variant: "ghost", size: "icon", className: "text-slate-200 hover:bg-slate-800 hover:text-white" })}
              onClick={() => setIsOpen(false)}
            >
              <Search />
            </Link>
            <Link to="/login" className={buttonVariants({ variant: "outline", className: "bg-slate-900 text-white border-slate-700 hover:bg-slate-800" })}>
              Login
            </Link>
            <Link to="/register" className={buttonVariants({ className: "bg-blue-600 text-white hover:bg-blue-700" })}>
              Register
            </Link>
          </div>

          <div className="flex items-center gap-2 lg:hidden">
            <Link
              to="/search"
              aria-label="Search"
              className={buttonVariants({ variant: "ghost", size: "icon", className: "text-slate-200 hover:bg-slate-800 hover:text-white" })}
            >
              <Search />
            </Link>
            <Button
              type="button"
              variant="outline"
              size="icon"
              className="border-slate-700 text-slate-200 hover:bg-slate-800 hover:text-white"
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
        <div className="border-t border-slate-800/30 bg-slate-950 lg:hidden">
          <Container className="py-4">
            <nav className="grid gap-1" aria-label="Mobile navigation">
              {NAVIGATION.map((item) => (
                <NavLink
                  key={item.name}
                  to={item.path}
                  onClick={() => setIsOpen(false)}
                  className={({ isActive }) =>
                    cn(
                      "rounded-md px-3 py-3 text-sm font-semibold text-slate-200 transition hover:bg-slate-800 hover:text-white",
                      isActive && "bg-blue-900/70 text-white",
                    )
                  }
                >
                  {item.name}
                </NavLink>
              ))}
            </nav>
            <div className="mt-4 grid gap-3 sm:grid-cols-2">
              <Link
                to="/login"
                onClick={() => setIsOpen(false)}
                className={buttonVariants({ variant: "outline", className: "w-full border-slate-700 text-slate-100 hover:bg-slate-800 hover:text-white" })}
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
            </div>
          </Container>
        </div>
      ) : null}
    </header>
  );
}
