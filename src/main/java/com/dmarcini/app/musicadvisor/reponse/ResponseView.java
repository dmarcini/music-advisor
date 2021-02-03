package com.dmarcini.app.musicadvisor.reponse;

import com.dmarcini.app.musicadvisor.MusicAdvisorOption;
import com.dmarcini.app.musicadvisor.webapi.Printable;

import java.util.ArrayList;
import java.util.List;

public class ResponseView {
    private final int entriesOnPage;

    private int currentPageNumber;
    private int allPagesNumber;

    private boolean wasTitlePrinted;
    private String title;

    private List<Printable> entries;

    public ResponseView(int entriesOnPage) {
        this.entriesOnPage = entriesOnPage;
    }

    public void setEntries(MusicAdvisorOption musicAdvisorOption, List<? extends Printable> entries) {
        this.wasTitlePrinted = false;
        this.title = "--- " + musicAdvisorOption.toString().replace("_", " ") + " ---";

        this.entries = new ArrayList<>(entries);

        this.currentPageNumber = 1;
        this.allPagesNumber = (int) Math.ceil(this.entries.size() / (float) entriesOnPage);

        print();
    }

    public void nextPage() {
        if (currentPageNumber == allPagesNumber) {
            System.out.println("It was last page.");
            return;
        }

        ++currentPageNumber;

        print();
    }

    public void prevPage() {
        if (currentPageNumber == 1) {
            System.out.println("It was first page.");
            return;
        }

        --currentPageNumber;

        print();
    }

    private void print() {
        int start = (currentPageNumber - 1) * entriesOnPage;
        int stop = (currentPageNumber == allPagesNumber) ?
                entries.size() :
                ((currentPageNumber - 1) * entriesOnPage) + entriesOnPage;

        System.out.println();

        if (!wasTitlePrinted) {
            System.out.println(title);
            System.out.println();

            wasTitlePrinted = true;
        }

        for (int i = start; i < stop; ++i) {
            entries.get(i).print();
        }

        System.out.println();
        System.out.println("--- PAGE " + currentPageNumber + " OF " + allPagesNumber + " ---");
    }
}
