import { createFileRoute } from "@tanstack/react-router"
import useTimetables, { Timetable } from "../../hooks/useTimetables"
import useLinkCoordinatorRequests from "../../hooks/useLinkCoordinatorRequests"
import { useState } from "react"

export const Route = createFileRoute("/_auth/request")({
  component: Request,
})

function Request() {
  const { allTimetables, refetch: refetchTimetables } = useTimetables()
  const { requests, createRequest, refetch: refetchRequests  } = useLinkCoordinatorRequests()

  const [isLoading, setIsLoading] = useState(false)
  const [search, setSearch] = useState("")

  const [timetable, setTimetable] = useState<Timetable>()
  const [telegramInput, setTelegramInput] = useState("")

  const onSubmit = async () => {
    if (!timetable) return

    setIsLoading(true)
    await createRequest({
      khnureTimetableId: timetable.id.toString(),
      telegramAccount: telegramInput,
    }).finally(() => setIsLoading(false))
    await refetchTimetables();
    await refetchRequests();

    setSearch("")
    setTimetable(undefined)
    setTelegramInput("")
    alert("Request was created")
  }

  const isTimetableRequested = (timetable: Timetable) =>
    requests.data?.find(request => request.id === timetable.id)?.requested

  const filteredTimetables = allTimetables.data?.filter(timetable =>
    timetable.name.toLowerCase().includes(search.toLowerCase())
  )

  const openChooseModal = () =>
    (
      document.getElementById("choose_timetable_modal_choose") as any
    ).showModal()
  const closeChooseModal = () =>
    (document.getElementById("choose_timetable_modal_choose") as any).close()

  return (
    <>
      <div className="space-y-4">
        <h2 className="text-lg font-semibold text-center">
          Request for the role of "Link Coordinator"
        </h2>
        <p>
          The role of "Link Coordinator" allows you to add links from the visit
          and links to online meetings (google meet, zoom meeting) for each pair
          from the discipline. Only the teacher can manage the teacher's links
          in the schedule after submitting the application. Several people from
          this group can manage group links in the schedule at the same time.
        </p>
        <p>
          For user verification, the administrator can ask to send a letter to a
          certain email address.
        </p>
        <p>
          To submit an application, select a schedule and wait for a response
          from the administrator.
        </p>

        <hr />
        <div className="flex flex-col gap-2 items-start">
          {timetable ? (
                <button
                    className="btn btn-sm btn-primary"
                    onClick={openChooseModal}
                >
                  Timetable: <strong>{timetable.name}</strong>
                </button>
          ) : (
              <button
                  className="btn btn-sm btn-primary"
                  onClick={openChooseModal}
              >
                Select a timetable
              </button>
          )}
          <label className="form-control w-full max-w-xs">
            <span className="label label-text">
              Telegram Account for contact
            </span>
            <input
              type="text"
              className="input input-sm input-bordered w-full max-w-xs"
              value={telegramInput}
              onChange={e => setTelegramInput(e.target.value)}
            />
          </label>

          <button
            className="btn btn-sm btn-primary"
            onClick={onSubmit}
            disabled={!timetable || isLoading}
          >
            {isLoading && <span className="loading loading-spinner"></span>}
            Submit request
          </button>
        </div>
      </div>

      <dialog id="choose_timetable_modal_choose" className="modal">
        <div className="modal-box space-y-4">
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
              ✕
            </button>
          </form>
          <h3 className="font-bold text-lg !mt-0">Select Timetable</h3>

          <input
            type="text"
            placeholder="Timetable Name"
            className="input input-bordered w-full max-w-xs"
            value={search}
            onChange={e => setSearch(e.target.value)}
          />
          <div className="h-full max-h-96 overflow-y-auto">
            <table className="table">
              <tbody>
                {filteredTimetables?.map(timetable => (
                  <tr
                    key={timetable.id}
                    className={
                      "hover" +
                      (isTimetableRequested(timetable) ? "" : " cursor-pointer")
                    }
                    onClick={() => {
                      if (isTimetableRequested(timetable)) {
                        return
                      }
                      setTimetable(timetable)
                      closeChooseModal()
                    }}
                  >
                    <td>{timetable.name}</td>
                    <td>
                      {isTimetableRequested(timetable) && (
                        <div className="badge badge-primary">Requested</div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="modal-action">
            <form method="dialog">
              {/* if there is a button in form, it will close the modal */}
              <button className="btn">Close</button>
            </form>
          </div>
        </div>
      </dialog>
    </>
  )
}
