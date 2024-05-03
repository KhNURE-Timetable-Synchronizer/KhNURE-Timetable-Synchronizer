import { useState } from "react"
import useTimetables, { Timetable } from "../hooks/useTimetables"

export default function AddTimetableButton() {
  const { allTimetables, addTimetable, refetchPersonal } = useTimetables()

  const [search, setSearch] = useState("")
  const [timetable, setTimetable] = useState<Timetable | null>(null)
  const [isLoading, setIsLoading] = useState(false)

  const filteredTimetables = allTimetables?.filter(timetable =>
    timetable.name.toLowerCase().includes(search.toLowerCase())
  )

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    setIsLoading(true)
    e.preventDefault()
    const form = e.currentTarget as HTMLFormElement
    const formData = new FormData(form)
    const startDate = formData.get("start_date") as string
    const endDate = formData.get("end_date") as string
    if (!timetable) return
    await addTimetable({
      timetableId: timetable.id,
      timetableType: timetable.type,
      startDate: new Date(startDate),
      endDate: new Date(endDate),
    })
    setIsLoading(false)
    closeModals()
    await refetchPersonal()
    setSearch("")
    setTimetable(null)
  }

  const openChooseModal = () =>
    (document.getElementById("add_timetable_modal_choose") as any).showModal()
  const openConfirmModal = () =>
    (document.getElementById("add_timetable_modal_confirm") as any).showModal()
  const closeModals = () => {
    ;(document.getElementById("add_timetable_modal_choose") as any).close()
    ;(document.getElementById("add_timetable_modal_confirm") as any).close()
  }

  return (
    <>
      <button
        className="btn btn-primary btn-sm w-full"
        onClick={openChooseModal}
      >
        Add Timetable
      </button>

      <dialog id="add_timetable_modal_choose" className="modal">
        <div className="modal-box space-y-4">
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
              ✕
            </button>
          </form>
          <h3 className="font-bold text-lg !mt-0">Add Timetable</h3>

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
                    className="hover cursor-pointer"
                    onClick={() => {
                      setTimetable(timetable)
                      openConfirmModal()
                    }}
                  >
                    <td>{timetable.name}</td>
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

      <dialog id="add_timetable_modal_confirm" className="modal">
        <div className="modal-box">
          <h3 className="font-bold text-lg">
            Choose dates to import {timetable?.name}
          </h3>
          <form
            onSubmit={onSubmit}
            id="add_timetable_dates_form"
            className="flex gap-2"
          >
            <label className="form-control w-full">
              <div className="label">
                <span className="label-text">Start Date</span>
              </div>
              <input
                type="date"
                placeholder="Start Date"
                name="start_date"
                className="input input-bordered w-full max-w-xs"
                defaultValue={new Date().toISOString().split("T")[0]}
                required
              />
            </label>

            <label className="form-control w-full">
              <div className="label">
                <span className="label-text">End Date</span>
              </div>
              <input
                type="date"
                placeholder="End Date"
                name="end_date"
                className="input input-bordered w-full max-w-xs"
                required
              />
            </label>
          </form>
          <div className="modal-action">
            <form method="dialog">
              {/* if there is a button in form, it will close the modal */}
              <button className="btn">Close</button>
            </form>

            <button
              type="submit"
              form="add_timetable_dates_form"
              className="btn btn-primary"
              disabled={isLoading}
            >
              {isLoading && <span className="loading loading-spinner"></span>}
              Add Timetable
            </button>
          </div>
        </div>
      </dialog>
    </>
  )
}
