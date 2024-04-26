import { useGoogleLogin } from "@react-oauth/google"
import Cookies from "js-cookie"
import { createContext, useContext, useState } from "react"

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL

const loginUrl = `${BACKEND_URL}/api/v1/jwt/create`
const cookieName = "JWT"

type AuthState = {
  isAuthenticated: boolean
  login: () => void
  isLoading: boolean
  logout: () => void
}

const AuthContext = createContext<AuthState | undefined>(undefined)

function AuthProvider({ children }: { children: React.ReactNode }) {
  const getCookie = () => Cookies.get(cookieName)

  const [jwt, setJwt] = useState<string | undefined>(getCookie())
  const [isLoading, setIsLoading] = useState(false)

  const refreshAuthCookie = () => {
    const cookie = getCookie()
    setJwt(cookie)
  }

  const login = useGoogleLogin({
    flow: "auth-code",
    onSuccess: async credentialResponse => {
      setIsLoading(true)

      const res = await fetch(loginUrl, {
        method: "POST",
        body: JSON.stringify(credentialResponse),
        headers: {
          "Content-Type": "application/json",
        },
      })

      if (!res.ok) {
        console.error("Login Failed Backend")
        setIsLoading(false)
        throw new Error("Login Failed Backend")
      }

      refreshAuthCookie()
      setIsLoading(false)
      window.location.reload()
    },
    onError: () => {
      console.error("Login Failed Google OAuth")
    },
    scope:
      "openid email profile https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.events",
  })

  const logout = () => {
    Cookies.remove(cookieName)
    refreshAuthCookie()
    window.location.reload()
  }

  return (
    <AuthContext.Provider
      value={{ isAuthenticated: !!jwt, login, isLoading, logout }}
    >
      {children}
    </AuthContext.Provider>
  )
}

function useAuth() {
  const context = useContext(AuthContext)

  if (context === undefined) {
    throw new Error("useAuth must be used within a AuthProvider")
  }

  return context
}

export { AuthProvider, useAuth, type AuthState }
