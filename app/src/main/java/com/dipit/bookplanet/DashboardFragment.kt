package com.dipit.bookplanet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DashboardFragment : Fragment() {
    lateinit var recyclerDash:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    val booklist= arrayListOf(
        "Case Closed Vol. 1",
        "The Kiss Quotient",
        "The Magic of Thinking Big",
        "Case Closed Vol. 2",
        "When Breath Becomes Air",
        "The 5AM Club",
        "The Murder of Roger Ackroyd",
        "The Bride Test",
        "Harry Potter and the Philosopher's Stone",
        "The Subtle Art of Not Giving a F*ck"
    )
    lateinit var recyclerAdapter: DashboardAdapter
    val bookInfoList= arrayListOf<Book>(
        Book("Case Closed Vol. 1","Gosho Aoyama","Rs 746","4.7",R.drawable.case_closed_1),
        Book("The Kiss Quotient","Helen Hoang","Rs 663","4.8",R.drawable.kiss_quetient),
        Book("The Magic of Thinking Big","David J. Schwartz","Rs 778","5.0",R.drawable.magic_of_thinking_big),
        Book("Case Closed Vol. 2","Gosho Aoyama","Rs 807","4.7",R.drawable.case_closed_2),
        Book("When Breath Becomes Air","Paul Kalanithi","Rs 479","4.4",R.drawable.when_breath_becomes_air),
        Book("The 5AM Club","Robin Sharma","Rs 247","4.5",R.drawable.club),
        Book("The Murder of Roger Ackroyd","Agatha Christie","Rs 288","4.2",R.drawable.murder),
        Book("The Bride Test","Helen Hoang","Rs 663","4.0",R.drawable.bride_test),
        Book("Harry Potter and the Philosopher's Stone","J. K. Rowling","Rs 327","4.5",R.drawable.pillopsopherstone),
        Book("The Subtle Art of Not Giving a F*ck","Mark Manson","Rs 444","4.3",R.drawable.orange_book)
        )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_dashboard,container,false)
        recyclerDash=view.findViewById(R.id.recyclerview)
        layoutManager=LinearLayoutManager(activity)
        recyclerAdapter= DashboardAdapter(activity as Context,bookInfoList)
        recyclerDash.adapter=recyclerAdapter
        recyclerDash.layoutManager=layoutManager
        recyclerDash.addItemDecoration(
            DividerItemDecoration(
                recyclerDash.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )
        return view
    }

}