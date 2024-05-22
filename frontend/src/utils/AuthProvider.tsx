import Cookies from "js-cookie"
import { createContext, useContext, useState } from "react"
import { decodeJwt } from "jose"

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL

const loginUrl = `${BACKEND_URL}/api/v1/jwt/create`
const cookieName = "JWT"

export type AuthUser = {
  email: string
  exp: number
  id: number
  refreshToken: string
  role: "USER" | "ADMIN"
}
type AuthState = {
  user?: AuthUser
  login: () => void
  loginProceedCode: (params: { code: string }) => void
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
  const user: AuthUser | undefined = jwt ? decodeJwt(jwt) : undefined

  const login = () => {
    setIsLoading(true)
    window.location.href = `https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?client_id=240421397087-jucg36d0qgmvcgc3tnqska5ne2vi9cld.apps.googleusercontent.com&scope=email%20profile%20openid%20https://www.googleapis.com/auth/calendar%20https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/calendar.events%20https://www.googleapis.com/auth/userinfo.profile&redirect_uri=${window.location.origin}/login/oauth&prompt=select_account&access_type=offline&response_type=code`
  }

  const loginProceedCode = async (params: { code: string }) => {
    if (isLoading) return
    setIsLoading(true)
    try {
      const res = await fetch(loginUrl, {
        method: "POST",
        body: JSON.stringify(params),
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      })
      if (!res.ok) throw new Error("Login Failed Backend")
    } catch (err) {
      alert("Login Failed Backend")
      window.location.pathname = "/login"
      throw new Error("Login Failed Backend")
    } finally {
      setIsLoading(false)
    }

    refreshAuthCookie()
    setIsLoading(false)
    window.location.pathname = "/"
  }

  const logout = () => {
    Cookies.remove(cookieName)
    refreshAuthCookie()
    window.location.reload()
  }

  return (
    <AuthContext.Provider
      value={{ user, login, loginProceedCode, isLoading, logout }}
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
