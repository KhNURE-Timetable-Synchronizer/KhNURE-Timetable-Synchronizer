import { useQuery } from "@tanstack/react-query"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/_auth/")({
  component: Home,
})

function Home() {
  const res = useQuery({
    queryKey: ["timetables"],
    queryFn: () => {
      return fetch(
        `${import.meta.env.VITE_BACKEND_URL}/api/v1/khnure/timetables`
      ).then(res => res.json())
    },
  })

  console.log({ res })

  return (
    <div className="p-2">
      <p>Hello from Home!</p>
      <pre>{JSON.stringify(res.data, null, 2)}</pre>
    </div>
  )
}
