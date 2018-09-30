CREATE TABLE Loan(
    LoanId number(12),
    Amount number(12),
    "Date" DATE,
    Loan_Title VARCHAR(128),
    Risk_Score number(12),
    Debt_To_Income_Ratio float(12),
    Zipcode number(12),
    State VARCHAR(2),
    Employment number(4),
    Policy_Code number(4),
    PRIMARY KEY (LoanId)
);

