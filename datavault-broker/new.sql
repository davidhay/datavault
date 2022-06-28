select
    vault0_.id as id1_28_0_,
    group1_.id as id1_16_1_,
    retentionp2_.id as id1_21_2_,
    dataset3_.id as id1_8_3_,
    user4_.id as id1_26_4_,
    vault0_.affirmed as affirmed2_28_0_,
    vault0_.contact as contact3_28_0_,
    vault0_.creationTime as creation4_28_0_,
    vault0_.dataset_id as dataset19_28_0_,
    vault0_.description as descript5_28_0_,
    vault0_.estimate as estimate6_28_0_,
    vault0_.grantEndDate as grantend7_28_0_,
    vault0_.group_id as group_i20_28_0_,
    vault0_.name as name8_28_0_,
    vault0_.notes as notes9_28_0_,
    vault0_.projectId as project10_28_0_,
    vault0_.pureLink as purelin11_28_0_,
    vault0_.retentionPolicy_id as retenti21_28_0_,
    vault0_.retentionPolicyExpiry as retenti12_28_0_,
    vault0_.retentionPolicyLastChecked as retenti13_28_0_,
    vault0_.retentionPolicyStatus as retenti14_28_0_,
    vault0_.reviewDate as reviewd15_28_0_,
    vault0_.snapshot as snapsho16_28_0_,
    vault0_.user_id as user_id22_28_0_,
    vault0_.vaultSize as vaultsi17_28_0_,
    vault0_.version as version18_28_0_,
    group1_.enabled as enabled2_16_1_,
    group1_.name as name3_16_1_,
    retentionp2_.dataGuidanceReviewed as dataguid2_21_2_,
    retentionp2_.description as descript3_21_2_,
    retentionp2_.endDate as enddate4_21_2_,
    retentionp2_.engine as engine5_21_2_,
    retentionp2_.extendUponRetrieval as extendup6_21_2_,
    retentionp2_.inEffectDate as ineffect7_21_2_,
    retentionp2_.minDataRetentionPeriod as mindatar8_21_2_,
    retentionp2_.minRetentionPeriod as minreten9_21_2_,
    retentionp2_.name as name10_21_2_,
    retentionp2_.sort as sort11_21_2_,
    retentionp2_.url as url12_21_2_,
    dataset3_.crisId as crisid2_8_3_,
    dataset3_.name as name3_8_3_,
    user4_.email as email2_26_4_,
    user4_.firstname as firstnam3_26_4_,
    user4_.lastname as lastname4_26_4_,
    user4_.password as password5_26_4_,
    user4_.properties as properti6_26_4_
from
    Vaults vault0_
        inner join
    Groups group1_
    on vault0_.group_id=group1_.id
        left outer join
    RetentionPolicies retentionp2_
    on vault0_.retentionPolicy_id=retentionp2_.id
        left outer join
    Datasets dataset3_
    on vault0_.dataset_id=dataset3_.id
        left outer join
    Users user4_
    on vault0_.user_id=user4_.id
where
    (
            group1_.id in (
            ?
            )
        )
  and (
            lower(vault0_.id) like ?
        or lower(vault0_.name) like ?
        or lower(vault0_.description) like ?
    )
order by
    vault0_.name asc limit ?,
    ?