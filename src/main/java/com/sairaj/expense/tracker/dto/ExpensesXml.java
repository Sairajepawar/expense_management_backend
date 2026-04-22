package com.sairaj.expense.tracker.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name="expenses")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpensesXml {
    @XmlElement(name = "expense")
    private List<ExpenseXml> expenses;
}
