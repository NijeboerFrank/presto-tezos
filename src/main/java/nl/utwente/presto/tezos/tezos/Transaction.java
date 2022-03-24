package nl.utwente.presto.tezos.tezos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private String hash;
    private String predecessor;
    private String successor;
    private String baker;
    private long height;
    private long cycle;
    private boolean isCycleSnapshot;
    private String time;
    private long solvetime;
    private long version;
    private long validationPass;
    private long fitness;
    private long priority;
    private String nonce;
    private String votingPeriodKind;
    private String slotMask;
    private long nEndorsedSlots;
    private long nOps;
    private long nOpsFailed;
    private long nOpsContract;
    private long nContractCalls;
    private long nTx;
    private long nActivation;
    private long nSeedNonceRevelations;
    private long nDoubleBakingEvidences;
    private long nDoubleEndorsementEvidences;
    private long nEndorsement;
    private long nDelegation;
    private long nReveal;
    private long nOrigination;
    private long nProposal;
    private long nBallot;
    private long nRegisterConstant;
    private double volume;
    private double fee;
    private double reward;
    private double deposit;
    private double unfrozenFees;
    private double unfrozenRewards;
    private double unfrozenDeposits;
    private double activatedSupply;
    private double burnedSupply;
    private long nAccounts;
    private long nNewAccounts;
    private long nNewContracts;
    private long nClearedAccounts;
    private long nFundedAccounts;
    private long gasLimit;
    private long gasUsed;
    private double gasPrice;
    private long storageSize;
    private double daysDestroyed;
    private double pctAccountReuse;
    private long nOpsImplicit;
    private boolean lbEscVote;
    private long lbEscEma;
}
