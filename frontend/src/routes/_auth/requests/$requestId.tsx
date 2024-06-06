import { createFileRoute, notFound } from "@tanstack/react-router"
import { z } from "zod"
import {
  RequestStatus,
  requestQueryOptions,
  updateRequestStatus,
  requestStatusesQueryOptions,
} from "../../../hooks/userRequestsAdmin"
import { useAuth } from "../../../utils/AuthProvider"
import { useState } from "react"
import { useQuery } from "@tanstack/react-query"

export const Route = createFileRoute("/_auth/requests/$requestId")({
  parseParams: params => ({
    requestId: z
      .number()
      .int()
      .parse(Number((params as any).requestId)),
  }),
  loader: async opts => {
    const request = await opts.context.queryClient.fetchQuery(
      requestQueryOptions(opts.params.requestId, opts.context.auth.logout)
    )
    if (!request) {
      throw notFound()
    }
    return { request }
  },
  component: Request,
})

function Request() {
  const { logout } = useAuth()
  const { requestId } = Route.useParams()
  const { request } = Route.useLoaderData()

  const statusesQuery = useQuery(requestStatusesQueryOptions(requestId, logout))

  const [status, setStatus] = useState<RequestStatus>(request.status)

  const onStatusChange = async (status: RequestStatus) => {
    const confirmRes = confirm(
      `Are you sure you want to change the status to ${status}?`
    )
    if (!confirmRes) {
      setStatus(request.status)
      return
    }
    await updateRequestStatus(request.id, status, logout)
    alert("Status updated")
    setStatus(status)
    await statusesQuery.refetch()
  }

  return (
    <div className="space-y-1">
      <p className="text-lg font-medium pb-4">
        Request for role "Link Coordinator"
      </p>
      <p>Id: {request.id}</p>
      <p>Email: {request.email}</p>
      <p>Requested timetable: {request.requestedTimetable.name}</p>
      <p>
        Telegram:{" "}
        {request.telegramAccount ? (
          <a
            href={`https://t.me/${request.telegramAccount}`}
            target="_blank"
            className="link"
          >
            @{request.telegramAccount}
          </a>
        ) : (
          "Not Provided"
        )}
      </p>
      <div>
        Status:{" "}
        <select
          className="select select-sm select-bordered w-full max-w-xs"
          value={status}
          onChange={async e => onStatusChange(e.target.value as RequestStatus)}
          disabled={!statusesQuery.data}
        >
          {[status, ...(statusesQuery.data?.nextAvailableStatusList ?? [])].map(
            status => (
              <option key={status} value={status}>
                {status}
              </option>
            )
          )}
        </select>
      </div>
    </div>
  )
}
