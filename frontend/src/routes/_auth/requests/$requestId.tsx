import { createFileRoute, notFound } from "@tanstack/react-router"
import { z } from "zod"
import {
  RequestStatuses,
  requestQueryOptions,
} from "../../../hooks/userRequestsAdmin"

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
  const { request } = Route.useLoaderData()

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
        {request.telegramAccount ? request.telegramAccount : "Not Provided"}
      </p>
      <div>
        Status:{" "}
        <select className="select select-sm select-bordered w-full max-w-xs">
          {RequestStatuses.map(status => (
            <option
              key={status}
              value={status}
              selected={status === request.status}
            >
              {status}
            </option>
          ))}
        </select>
      </div>
    </div>
  )
}
