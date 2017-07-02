package com.lancel.bookshelf.web;

import com.lancel.bookshelf.core.entities.Book;
import com.lancel.bookshelf.services.BookService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HomePage extends WebPage {

	private Logger log = LoggerFactory.getLogger(HomePage.class);

	@SpringBean
	BookService bookService;

	private IModel<String> searchText = Model.of("");
	private IModel<String> searchedBy = Model.of("");
	private IModel<String> sortedBy = Model.of("id");
	private IModel<Boolean> isAscending = Model.of(Boolean.TRUE);

	private IModel<String> titleInput = Model.of("");
	private IModel<String> isbnInput = Model.of("");
	private IModel<String> authorInput = Model.of("");

	List<IModel<Book>> tableModel = new ArrayList<>();
	RefreshingView<Book> view;

	public HomePage(final PageParameters parameters) {
		super(parameters);
		Form form = new Form("form");
		add(form);

		FeedbackPanel feedback = new FeedbackPanel("feedback");
		form.add(feedback);

		form.add(new AjaxLink("sortById") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				sortedBy.setObject("id");
				changeOrder();
				sortTableModel();
				target.add(form);
			}
		});

		form.add(new AjaxLink("sortByTitle") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				sortedBy.setObject("title");
				changeOrder();
				sortTableModel();
				target.add(form);
			}
		});

		form.add(new AjaxLink("sortByIsbn") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				sortedBy.setObject("isbn");
				changeOrder();
				sortTableModel();
				target.add(form);
			}
		});

		form.add(new AjaxLink("sortByAuthor") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				sortedBy.setObject("author");
				changeOrder();
				sortTableModel();
				target.add(form);
			}
		});

		view = new RefreshingView<Book>("books") {
			@Override
			protected Iterator<IModel<Book>> getItemModels() {
				loadTableModel();
				return tableModel.iterator();
			}

			@Override
			protected void populateItem(Item<Book> item) {

				final IModel<Boolean> editMode = Model.of(Boolean.FALSE);

				IModel<Book> book = item.getModel();
				item.add(new Label("id", new PropertyModel<>(book, "id")));

				TextField isbn = new TextField<String>("isbn", new PropertyModel<>(book, "isbn")) {
					@Override
					public boolean isEnabled() {
						return editMode.getObject();
					}

					@Override
					protected void onModelChanged() {
						bookService.updateBook(book.getObject());
					}
				};
				isbn.setRequired(true);
				isbn.setOutputMarkupId(true);
				item.add(isbn);

				TextField title = new TextField<String>("title", new PropertyModel<>(book, "title")) {
					@Override
					public boolean isEnabled() {
						return editMode.getObject();
					}

					@Override
					protected void onModelChanged() {
						bookService.updateBook(book.getObject());
					}
				};
				title.setRequired(true);
				title.setOutputMarkupId(true);
				item.add(title);

				TextField author = new TextField<String>("author", new PropertyModel<>(book, "author")) {
					@Override
					public boolean isEnabled() {
						return editMode.getObject();
					}

					@Override
					protected void onModelChanged() {
						bookService.updateBook(book.getObject());
					}
				};
				author.setRequired(true);
				author.setOutputMarkupId(true);
				item.add(author);

				AjaxLink editLink = new AjaxLink("editLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						editMode.setObject(Boolean.TRUE);
						target.add(isbn, title, author);
					}
				};
				item.add(editLink);

				item.add(new AjaxLink("removeLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						bookService.deleteBook(book.getObject().getId());
						target.add(form);
					}
				});
			}
		};
		form.add(view);

		List<String> attributes = Arrays.asList("", "id", "title", "isbn", "author");

		DropDownChoice<String> serachAttribute = new DropDownChoice<>("searchedBy", searchedBy, attributes);
		form.add(serachAttribute);

		DropDownChoice<String> sortAttribute = new DropDownChoice<>("sortedBy", sortedBy, attributes);
		form.add(sortAttribute);

		List<Boolean> booleans = Arrays.asList(Boolean.TRUE, Boolean.FALSE);
		DropDownChoice<Boolean> sortAsc = new DropDownChoice<>("asc", isAscending, booleans, new ChoiceRenderer<Boolean>(){
			@Override
			public Object getDisplayValue(Boolean object) {
				if (Boolean.TRUE.equals(object)) {
					return "aufsteigend";
				} else {
					return "absteigend";
				}
			}
		});
		form.add(sortAsc);

		TextField<String> search = new TextField<String>("search", searchText);
		form.add(search);

		form.add(new Button("submit") {
			@Override
			public void onSubmit() {
				log.info("submit");
			}
		});

		form.add(new Button("sort") {
			@Override
			public void onSubmit() {
				log.info("sort");
			}
		});

		Form addForm = new Form("addForm") {
			@Override
			protected void onSubmit() {
				bookService.addBook(titleInput.getObject(), isbnInput.getObject(), authorInput.getObject());
				titleInput.setObject(null);
				isbnInput.setObject(null);
				authorInput.setObject(null);
			}
		};
		add(addForm);

		TextField<String> titleIn = new TextField<>("titleInput", titleInput);
		titleIn.setRequired(true);
		addForm.add(titleIn);

		TextField<String> isbnIn = new TextField<>("isbnInput", isbnInput);
		isbnIn.setRequired(true);
		addForm.add(isbnIn);

		TextField<String> authorIn = new TextField<>("authorInput", authorInput);
		authorIn.setRequired(true);
		addForm.add(authorIn);

		addForm.add(new FeedbackPanel("addFeedback"));


	}

	private void changeOrder() {
		if (Boolean.TRUE.equals(isAscending.getObject())) {
			isAscending.setObject(Boolean.FALSE);
		} else {
			isAscending.setObject(Boolean.TRUE);
		}
	}

	private void loadTableModel() {
		List<Book> books = bookService.findAll(searchedBy.getObject(), searchText.getObject() == null? "": searchText.getObject());
		tableModel.clear();
		if (books.size() > 0) {
			for (Book book: books) {
				tableModel.add(Model.of(book));
			}
		}
	}

	private void sortTableModel() {
		tableModel.sort((o1, o2) -> {
			if ("id".equals(sortedBy.getObject())) {
				if (Boolean.TRUE.equals(isAscending.getObject())) {
					return new Long(o1.getObject().getId()).compareTo(new Long(o2.getObject().getId()));
				} else {
					return -new Long(o1.getObject().getId()).compareTo(new Long(o2.getObject().getId()));
				}
			}
			return 0;
		});
		view.modelChanged();
	}

}
