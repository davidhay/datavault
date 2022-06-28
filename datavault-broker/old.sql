select
    this_.id as id1_28_4_,
    this_.affirmed as affirmed2_28_4_,
    this_.contact as contact3_28_4_,
    this_.creationTime as creation4_28_4_,
    this_.dataset_id as dataset19_28_4_,
    this_.description as descript5_28_4_,
    this_.estimate as estimate6_28_4_,
    this_.grantEndDate as grantend7_28_4_,
    this_.group_id as group_i20_28_4_,
    this_.name as name8_28_4_,
    this_.notes as notes9_28_4_,
    this_.projectId as project10_28_4_,
    this_.pureLink as purelin11_28_4_,
    this_.retentionPolicy_id as retenti21_28_4_,
    this_.retentionPolicyExpiry as retenti12_28_4_,
    this_.retentionPolicyLastChecked as retenti13_28_4_,
    this_.retentionPolicyStatus as retenti14_28_4_,
    this_.reviewDate as reviewd15_28_4_,
    this_.snapshot as snapsho16_28_4_,
    this_.user_id as user_id22_28_4_,
    this_.vaultSize as vaultsi17_28_4_,
    this_.version as version18_28_4_,
    dataset3_.id as id1_8_0_,
    dataset3_.crisId as crisid2_8_0_,
    dataset3_.name as name3_8_0_,
    group1_.id as id1_16_1_,
    group1_.enabled as enabled2_16_1_,
    group1_.name as name3_16_1_,
    retentionp5_.id as id1_21_2_,
    retentionp5_.dataGuidanceReviewed as dataguid2_21_2_,
    retentionp5_.description as descript3_21_2_,
    retentionp5_.endDate as enddate4_21_2_,
    retentionp5_.engine as engine5_21_2_,
    retentionp5_.extendUponRetrieval as extendup6_21_2_,
    retentionp5_.inEffectDate as ineffect7_21_2_,
    retentionp5_.minDataRetentionPeriod as mindatar8_21_2_,
    retentionp5_.minRetentionPeriod as minreten9_21_2_,
    retentionp5_.name as name10_21_2_,
    retentionp5_.sort as sort11_21_2_,
    retentionp5_.url as url12_21_2_,
    user6_.id as id1_26_3_,
    user6_.email as email2_26_3_,
    user6_.firstname as firstnam3_26_3_,
    user6_.lastname as lastname4_26_3_,
    user6_.password as password5_26_3_,
    user6_.properties as properti6_26_3_
from
    Vaults this_
    left outer join Datasets dataset3_ on this_.dataset_id=dataset3_.id
    inner join Groups group1_ on this_.group_id=group1_.id left outer join
    RetentionPolicies retentionp5_ on this_.retentionPolicy_id=retentionp5_.id left outer join
    Users user6_ on this_.user_id=user6_.id
    where
        group1_.id in (
        ?
        )
    and (
            lower(this_.id) like ?
        or lower(this_.name) like ?
        or lower(this_.description) like ?
    )
order by
    this_.name asc limit ?,
    ?