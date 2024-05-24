import { createFileRoute } from "@tanstack/react-router"
import { useEffect } from "react"
import { z } from "zod"
import { useAuth } from "../../utils/AuthProvider"

const searchParamsSchema = z.object({
  code: z.string().optional(),
  error: z.string().optional(),
})

export const Route = createFileRoute("/login/oauth")({
  component: OAuth,
  validateSearch: searchParamsSchema,
})

function OAuth() {
  const searchParams = Route.useSearch()
  const { isLoading, loginProceedCode } = useAuth()

  useEffect(() => {
    loginProceedCode({ code: searchParams.code, error: searchParams.error })
  }, [])

  return (
    <div className="w-full h-full flex justify-center items-center">
      <button className="btn btn-sm btn-primary" disabled={isLoading}>
        {isLoading && <span className="loading loading-spinner" />}
        Logging in...
      </button>
    </div>
  )
}
