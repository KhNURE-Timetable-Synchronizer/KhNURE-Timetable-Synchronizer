import {
  createRootRouteWithContext,
  Link,
  Outlet,
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
    const { isAuthenticated, logout } = useAuth()

    return (
      <>
        <div className="p-4 flex justify-between gap-2">
          <Link to="/" className="text-xl font-bold text-blue-700 font-mono">
            Timetable Synchronizer
          </Link>
          {isAuthenticated && (
            <Link to="/login" className="btn btn-sm" onClick={logout}>
              Logout
            </Link>
          )}
        </div>
        <hr className="mb-2" />
        <Outlet />
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
