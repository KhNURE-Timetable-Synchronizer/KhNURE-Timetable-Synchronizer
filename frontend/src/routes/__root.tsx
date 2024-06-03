import {
  createRootRouteWithContext,
  Link,
  Outlet,
  useRouterState,
} from "@tanstack/react-router"
import React, { Suspense } from "react"
import { AuthState, useAuth } from "../utils/AuthProvider"
import { QueryClient } from "@tanstack/react-query"

interface RootRouterContext {
  auth: AuthState
  queryClient: QueryClient
}

export const Route = createRootRouteWithContext<RootRouterContext>()({
  component: () => {
    const { user, logout } = useAuth()
    const router = useRouterState()

    const linkClassname = (path: string) =>
      "btn btn-sm" +
      (router.location.pathname === path ? " btn-neutral" : " btn-ghost")

    return (
      <>
        <div className="w-full max-w-6xl mx-auto p-4 flex justify-between gap-2">
          <div className="flex gap-2 items-center">
            <div className="flex gap-1">
              <p className="text-2xl font-mono hidden md:block">KhNURE</p>
              <div>
                <p className="text-xs">Timetable</p>
                <p className="text-xs">Synchronizer</p>
              </div>
            </div>

            <Link to="/" className={linkClassname("/")}>
              Home
            </Link>
            {user?.role === "ADMIN" && (
              <>
                <Link to="/users" className={linkClassname("/users")}>
                  Users
                </Link>
                <Link to="/requests" className={linkClassname("/requests")}>
                  Requests
                </Link>
              </>
            )}
            {(user?.role === "USER" || user?.role === "LINKS_COORDINATOR") && (
              <Link to="/request" className={linkClassname("/request")}>
                Be a Link Coordinator
              </Link>
            )}
          </div>
          {user && (
            <div className="flex gap-2 items-center">
              <p className="text-xs sm:text-sm">{user.email}</p>
              <Link to="/login" className="btn btn-sm" onClick={logout}>
                Logout
              </Link>
            </div>
          )}
        </div>
        <hr className="mb-2" />
        <div className="w-full max-w-6xl mx-auto p-2">
          <Outlet />
        </div>
        <Suspense>
          <TanStackRouterDevtools />
        </Suspense>
      </>
    )
  },
})

const TanStackRouterDevtools =
  process.env.NODE_ENV === "production"
    ? () => null // Render nothing in production
    : React.lazy(() =>
        // Lazy load in development
        import("@tanstack/router-devtools").then(res => ({
          default: res.TanStackRouterDevtools,
          // For Embedded Mode
          // default: res.TanStackRouterDevtoolsPanel
        }))
      )
