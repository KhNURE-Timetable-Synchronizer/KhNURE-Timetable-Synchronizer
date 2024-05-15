import { createFileRoute, redirect } from "@tanstack/react-router"
import { useAuth } from "../utils/AuthProvider"

export const Route = createFileRoute("/login")({
  beforeLoad: async ({ context }) => {
    if (context.auth.user) {
      throw redirect({
        code: 403,
        to: "/",
      })
    }
  },
  component: Login,
})

function Login() {
  const { isLoading, login } = useAuth()

  return (
    <div className="w-full h-full flex justify-center items-center">
      <button onClick={login} className="btn btn-sm btn-primary" disabled={isLoading}>
        {isLoading && <span className="loading loading-spinner" />}
        Login with Google
      </button>
    </div>
  )
}
